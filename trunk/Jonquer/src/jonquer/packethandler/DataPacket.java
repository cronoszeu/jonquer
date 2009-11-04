package jonquer.packethandler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import jonquer.model.Monster;
import jonquer.model.Npc;
import jonquer.model.Player;
import jonquer.model.Character;
import jonquer.model.World;
import jonquer.util.Formula;
import jonquer.util.Log;
import jonquer.util.StaticData;

public class DataPacket implements PacketHandler {

    public void handlePacket(Player player, byte[] packet) throws Exception {

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

            case 81: // actions
                player.getCharacter().setAction(bb.get(12));
                for (Player p : player.getPlayersInView()) {
                    if (p != player) {
                        if (p.getCharacter().getMapid() == player.getCharacter().getMapid()) {
                            if (Formula.inview(p.getCharacter(), player.getCharacter())) {
                                p.getActionSender().write(ByteBuffer.wrap(bb.array().clone()));
                            }
                        }
                    }
                }
                //player.updateMeToOthers();
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
                for (Npc npc : StaticData.npcs) {
                    if (npc.getMapid() == player.getCharacter().getMapid()) {
                        if (Formula.distance(player.getCharacter().getX(), player.getCharacter().getY(), npc.getCellx(), npc.getCelly()) <= Character.VIEW_RANGE) {
                            player.getActionSender().sendNpcSpawn(npc.getId(), npc.getCellx(), npc.getCelly(), npc.getLookface(), 1, npc.getType());
                        }
                    }
                }
                for (Monster monster : World.getWorld().getMonsters()) {
                    if (monster.getMap() == player.getCharacter().getMapid() && monster != null) {
                        if (Formula.distance(player.getCharacter().getX(), player.getCharacter().getY(), monster.getX(), monster.getY()) <= Character.VIEW_RANGE) {
                            player.getActionSender().sendMonsterSpawn(monster);
                        }
                    }
                }
                player.getActionSender().write(bb);
                break;

            case 130: // Complete login.

                break;

            case 133: // jump
                short prevX = bb.getShort(0x10);
                short prevY = bb.getShort(0x12);
                short nextX = bb.getShort(0xc);
                short nextY = bb.getShort(0xe);
                Log.debug(prevX + " - " + prevY);
                if (prevX != player.getCharacter().getX() || prevY != player.getCharacter().getY()) {
                    Log.hack(player.getCharacter().getName() + " sent invalid old coordinates (From X: " + prevX + ", Y: " + prevY + "  To X: " + nextX + ", Y: " + nextY + ")");
                } else {
                    if (Math.abs(prevX - nextX) > 22 || Math.abs(prevY - nextY) > 22) {
                        player.destroy();
                    } else {
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
