package jonquer.packethandler;

import java.nio.ByteOrder;

import jonquer.model.Player;
import jonquer.model.World;
import jonquer.util.Formula;
import jonquer.util.Log;

import java.nio.ByteBuffer;

public class ChatHandler implements PacketHandler {

    @Override
    public int getPacketID() {
	return 1004;
    }

    @Override
    public void handlePacket(Player player, byte[] packet) throws Exception {
	ByteBuffer bb = ByteBuffer.wrap(packet);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	int type = bb.getShort(8);
	int pos = 26;
	System.out.println("Real: " + bb.getShort(0) + "  BB: " + packet.length);
	try {
	String from = new String(bb.array(), pos, bb.get(25));
	pos+=from.length();
	String to = new String(bb.array(), pos + 1, bb.get(pos));
	int temp = pos + to.length();
	pos+=to.length() + 2;
	
	String msg = new String(bb.array(), pos + 1, bb.get(pos));
	if(msg.substring(0, 1).equals("/")) {
	    CommandHandler.handleCmd(msg, player);
	    return;
	} else {

	    Log.log(msg);
	    // wisper
	    if(type == Formula.WHISPER_MESSAGE_TYPE) {
		for(Player p : World.getWorld().getPlayers()) {
		    if(p.getCharacter().getName().equals(to)) {
			p.getActionSender().sendMessage(0xFFFF, Formula.WHISPER_MESSAGE_TYPE, player.getCharacter().getName(), p.getCharacter().getName(), msg);
			return;
		    }
		}
	    }
	    // talk
	    if(type == Formula.TALK_MESSAGE_TYPE) {
		for(Player p : World.getWorld().getPlayers()) {
		    if(p.getCharacter().getMap() == player.getCharacter().getMap() && p != player) {
			if(Math.abs(p.getCharacter().getX() - player.getCharacter().getX()) <= 25 && Math.abs(p.getCharacter().getY() - player.getCharacter().getY()) <= 25) {
			    p.getActionSender().sendMessage(0xFFFFF, Formula.TALK_MESSAGE_TYPE, player.getCharacter().getName(), to, msg);
			}
		    }
		}
	    }
	}
	} catch(Exception e) {
	    e.printStackTrace();
	}

    }

}
