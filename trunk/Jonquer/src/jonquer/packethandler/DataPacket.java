package jonquer.packethandler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import jonquer.debug.Log;
import jonquer.misc.Formula;
import jonquer.model.Player;
import jonquer.model.Portal;
import jonquer.model.World;
import jonquer.model.future.RollingDelay;

public class DataPacket implements PacketHandler {

    public void handlePacket(final Player player, byte[] packet) throws Exception {

        ByteBuffer bb = ByteBuffer.wrap(packet);
        bb.order(ByteOrder.LITTLE_ENDIAN);


        switch (bb.getShort(22)) {

            case 74:
                player.getActionSender().sendLocation();
                break;

            case 76:
                player.getActionSender().write(bb);
                break;

            case 78:
                player.getActionSender().write(bb);
                break;

            case 85:
                int timestamp = bb.getInt(4);
                int playerid = bb.getInt(8);
                short cellx = bb.getShort(16);
                short celly = bb.getShort(18);
                if (player.getCharacter().getID() == playerid) {
                    Portal portal = null;
                    for (Portal p : World.getWorld().getPortals()) {
                        if (player.getCharacter().getMapid() == p.getMapid() &&
                                Formula.distance(cellx, celly,
                                p.getCellx(), p.getCelly()) <= 1) {
                            portal = p;
                        }
                    }
                    if(portal != null) {
                        for(Player entity : player.getMap().getPlayers().values()) {
                            if(entity != player &&
                                    entity.getCharacter().inview(player.getCharacter())) {
                                entity.getActionSender().removeEntity(player);
                            }
                        }
                        player.getCharacter().setMapid(portal.getTarget_mapid());
                        player.getCharacter().setX(portal.getTarget_cellx());
                        player.getCharacter().setY(portal.getTarget_celly());
                        player.getActionSender().sendLocation();
                        player.getActionSender().sendCompleteMapChange();
                        player.getActionSender().sendMapInfo();
                        player.updateGroundItems();
                        player.updateMeToOthers();
                        player.updateMonsters();
                        player.updateNpcs();
                        player.updateOthersToMe();
                    }
                } else {
                    player.destroy();
                }
                break;


            case 117:
                int uid = bb.getInt(12);
                Player pl = player.getMap().getPlayers().get(uid);
                if (pl != null) {
                    pl.getActionSender().sendSystemMessage(player.getCharacter().getName() + " is viewing your equipment.");
                    player.getActionSender().sendViewedEquips(pl);
                }
                break;

            case 79:
                int dir = bb.get(20);
                if (dir < 0 || dir > 7) {
                    return;
                }
                player.getCharacter().setDirection((byte) dir);
                player.move(-1, -1, bb);
                break;

            case 81: // actions
        	int prev = player.getCharacter().getAction();
        	int id = bb.get(12);
                player.getCharacter().setAction(id);
                for (Player p : player.getMap().getPlayers().values()) {
                    if (p != player) {
                        if (Formula.inView(p.getCharacter(), player.getCharacter())) {
                            p.getActionSender().write(bb);
                        }
                    }
                }
                if(id == -6 && prev != -6) { // sit (recovers stamina)
                    World.getWorld().getTimerService().add(new RollingDelay(1000, 10) {
                	public void execute() {
                	    if(player.getCharacter().getAction() == -6) {
                		player.getCharacter().setStamina(player.getCharacter().getStamina() + 10);
                		if(player.getCharacter().getStamina() > 100) {
                		    player.getCharacter().setStamina(100);
                		    rolling = false;
                		}
                		player.getActionSender().sendStamina();
                	    } else rolling = false;
                	}
                    });
                }
                break;

            case 96:
                player.getActionSender().write(bb);
                break;

            case 97:
                player.getActionSender().write(bb);
                break;

            case 114: // get surroundings
                for (Player p : World.getWorld().getPlayers()) {
                    if (p.getCharacter().getMapid() == player.getCharacter().getMapid()) {
                        if (p != player) {
                            p.getActionSender().sendSpawnPacket(player.getCharacter());
                            player.getActionSender().sendSpawnPacket(p.getCharacter());
                        }
                    }
                }
                player.updateNpcs();
                player.updateMonsters();
                player.updateGroundItems();
                player.getActionSender().write(bb);
                break;

            case 130: // Complete login.
                player.getActionSender().write(bb);
                break;

            case 133: // jump
                short prevX = bb.getShort(0x10);
                short prevY = bb.getShort(0x12);
                short nextX = bb.getShort(0xc);
                short nextY = bb.getShort(0xe);

                if (prevX != player.getCharacter().getX() || prevY != player.getCharacter().getY()) {
                    Log.hack(player.getCharacter().getName() + " sent invalid old coordinates (From X: " + prevX + ", Y: " + prevY + "  To X: " + nextX + ", Y: " + nextY + ")");
                } else {
                    if (Math.abs(prevX - nextX) > 22 || Math.abs(prevY - nextY) > 22) {
                        player.destroy();
                    } else {
                        int direction = 0;
                        if (nextX == prevX && nextY < prevY) {
                            direction = 4;
                        } else if (nextX == prevX && nextY > prevY) {
                            direction = 0;
                        } else if (nextX < prevX && nextY == prevY) {
                            direction = 2;
                        } else if (nextX > prevX && nextY == prevY) {
                            direction = 6;
                        } else if (nextX > prevX && nextY > prevY) {
                            direction = 7;
                        } else if (nextX < prevX && nextY < prevY) {
                            direction = 3;
                        } else if (nextX < prevX && nextY > prevY) {
                            direction = 3;
                        } else {
                            direction = 5;
                        }
                        player.getCharacter().setDirection((byte) direction);
                        player.getCharacter().setX(nextX);
                        player.getCharacter().setY(nextY);
                        player.getCharacter().setAction(100);
                        player.move(prevX, prevY, bb);
                    }
                }
                break;

            default:
                Log.log("Unhandled Data Packet (" + bb.getShort(22) + ")");
        }
    }

    public int getPacketID() {
        return 1010;
    }
}
