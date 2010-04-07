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
	int subID = bb.getShort(22);
	Log.debug("Data Packet ID: " + subID);
	switch (subID) {

	case 94: // revive
	    if(System.currentTimeMillis() - player.lastDeath < 20000) {
		Log.hack(player.getCharacter().getName() + " tried to revive in under 20 seconds");
		return;
	    }
	    player.getCharacter().setDead(false);
	    player.getCharacter().setLook(player.lastModel);
	    int x = 429;
	    int y = 380;
	    for(Player p : player.getMap().getPlayers().values()) {
		if(p.getCharacter().inview(player.getCharacter()) && !Formula.inView(x, y, p.getX(), p.getY())) {
		    p.getActionSender().removeEntity(player);
		    player.getActionSender().removeEntity(p);
		} 
	    }
	    player.getCharacter().setX((short)x);
	    player.getCharacter().setY((short)y);
	    player.getActionSender().status1(player.getCharacter().getID(), 0);
	    player.getActionSender().status3(player.getCharacter().getID());
	    player.getActionSender().sendHeroInfo();
	    player.getActionSender().status(player.getCharacter().getID(), 26, 0);
	    player.setCurHP(player.getCharacter().getMaxlife());
	    player.updateNpcs();
	    player.updateMeToOthers();
	    player.updateOthersToMe();
	    player.updateMonsters();
	    player.updateGroundItems();
	   
	    break;

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
			if(entity != player && entity.getCharacter().inview(player.getCharacter())) {
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
		    player.updateOthersToMe();
		    player.updateMonsters();
		    player.updateNpcs();
		    
		    

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
	    player.checkAndStopAttack();
	    int prev = player.getCharacter().getAction();
	    int id = bb.get(12);
	    player.getCharacter().setAction(id);
	    //player.getActionSender().write(bb);
	    for (Player p : player.getMap().getPlayers().values()) {
		if(p != player)
		    if (Formula.inView(p.getCharacter(), player.getCharacter())) {
			p.getActionSender().write(bb);
		    }
	    }
	    if(id == -6 && prev != -6) { // sit (recovers stamina)
		World.getWorld().getTimerService().add(new RollingDelay(1000, 10) {
		    public void execute() {
			if(player.getCharacter().getAction() == -6) {
			    player.getCharacter().setStamina(player.getCharacter().getStamina() + 10);
			    if(player.getCharacter().getStamina() > 100) {
				player.getCharacter().setStamina(100);
				stop();
			    }
			    player.getActionSender().sendStamina();
			} else stop();
		    }
		});
	    }
	    break;
	case 96: // change fight mode

	    int mode = bb.get(12);
	    if(mode < 0 || mode > 4) {
		Log.hack(player.getCharacter().getName() + " tried to send an invalid fight mode");
		player.destroy();
		return;
	    }
	    player.getCharacter().setFightmode(mode);
	    player.getActionSender().write(bb);
	    if(mode == Formula.MODE_PK)
		player.getActionSender().sendSystemMessage("Free PK mode. You can attack anyone.");
	    else if(mode == Formula.MODE_CAPTURE)
		player.getActionSender().sendSystemMessage("Restrictive PK mode. You can only attack monsters, black-name and blue-name players.");
	    else if(mode == Formula.MODE_TEAM)
		player.getActionSender().sendSystemMessage("Team PK mode. YOu can attack monsters and players except for your teammates.");
	    else if(mode == Formula.MODE_PEACE)
		player.getActionSender().sendSystemMessage("Peace mode. You can only attack monsters and won't hurt other players.");
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
		    player.checkAndStopAttack();
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
