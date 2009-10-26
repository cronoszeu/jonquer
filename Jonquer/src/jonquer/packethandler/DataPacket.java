package jonquer.packethandler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import jonquer.model.Player;
import jonquer.util.Log;

public class DataPacket implements PacketHandler {

	public void handlePacket(Player player, byte[] packet) {

		ByteBuffer bb = ByteBuffer.wrap(packet);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		switch(bb.getShort(22)) {

		case 74:
			player.getActionSender().sendLocation();
			break;
			
		case 133: // jump
		    short prevX = bb.getShort(0x10);
		    short prevY = bb.getShort(0x12);
		    short nextX = bb.getShort(0xc);
		    short nextY = bb.getShort(0xe);
		    Log.log(prevX + " - " + prevY);
		    if(prevX != player.getCharacter().getX() || prevY != player.getCharacter().getY()) {
			Log.hack(player.getCharacter().getName() + " sent invalid old coordinates (From X: " + prevX + ", Y: " + prevY + "  To X: " + nextX + ", Y: " + nextY + ")" );
		    } else {
			if(Math.abs(prevX - nextX) > 22 || Math.abs(prevY - nextY) > 22) {
			    player.destroy();
			} else {
			    player.getCharacter().setX(nextX);
			    player.getCharacter().setY(nextY);
			    // inset updating other players of your jump here.
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
