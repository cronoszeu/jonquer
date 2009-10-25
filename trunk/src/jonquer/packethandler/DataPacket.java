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
	    
	default:
	    Log.debug("Unhandled Data Packet (" + bb.getShort(22) + ")");
	}
    }

    public int getPacketID() {
	return 1010;
    }

}
