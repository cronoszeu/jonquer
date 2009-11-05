package jonquer.packethandler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import jonquer.model.Player;

public class FrameHandler implements PacketHandler {

    @Override
    public int getPacketID() {
	return 1009;
    }

    @Override
    public void handlePacket(Player player, byte[] packet) {
	
	ByteBuffer bb = ByteBuffer.wrap(packet);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	int id = bb.getInt(0x0c);
	if(id == 4) {
	    EquipHandler.handlePacket(player, bb);
	    return;
	}
	
	System.out.println("Frame handler: ID " + id);
    }

}
