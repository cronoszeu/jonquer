package jonquer.packethandler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import jonquer.model.Player;

public class DialogActionHandler implements PacketHandler {

    @Override
    public int getPacketID() {
	return 2032;
    }

    @Override
    public void handlePacket(Player player, byte[] packet) throws Exception {
	ByteBuffer bb = ByteBuffer.wrap(packet);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	player.setLastOption(bb.get(10));
    }

}
