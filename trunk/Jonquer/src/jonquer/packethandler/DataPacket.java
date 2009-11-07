package jonquer.packethandler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import jonquer.debug.Log;
import jonquer.misc.Formula;
import jonquer.misc.StaticData;
import jonquer.model.Monster;
import jonquer.model.Npc;
import jonquer.model.Player;
import jonquer.model.Character;
import jonquer.model.World;

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
	    
	case 117:
	    
	    int uid = bb.getInt(12);
	    Player pl = player.getMap().getPlayers().get(uid);
	    if(pl != null) {
		pl.getActionSender().sendSystemMessage(player.getCharacter().getName() + " is viewing your equipment.");
		player.getActionSender().sendViewedEquips(pl);
	    }
	    break;
	    
	case 79:
	    
	    int dir = bb.get(20);
	    if(dir < 0 || dir > 7)
		return;
	    System.out.println(dir);
	    player.getCharacter().setDirection((byte)dir);
	    player.move(-1, -1, bb);
	    break;

	case 81: // actions
	    player.getCharacter().setAction(bb.get(12));
	    for (Player p : player.getMap().getPlayers().values()) {
		if (p != player) {
			if (Formula.inView(p.getCharacter(), player.getCharacter())) {
			    p.getActionSender().write(bb);
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
	    player.updateNpcs();
	    player.updateMonsters();
	    player.updateGroundItems();
	    player.getActionSender().write(bb);
	    break;

	case 130: // Complete login.

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
		    if(nextX == prevX && nextY < prevY)
			direction = 4;
		    else if(nextX == prevX && nextY > prevY)
			direction = 0;
		    else if(nextX < prevX && nextY == prevY)
			direction = 2;
		    else if(nextX > prevX && nextY == prevY)
			direction = 6;
		    else if(nextX > prevX && nextY > prevY)
			direction = 7;
		    else if(nextX < prevX && nextY < prevY)
			direction = 3;
		    else if(nextX < prevX && nextY > prevY)
			direction = 3;
		    else
			direction = 5;
		    player.getCharacter().setDirection((byte)direction);
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
