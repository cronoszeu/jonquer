package jonquer.packethandler;

import jonquer.model.Player;

public class ChatHandler implements PacketHandler {

	@Override
	public int getPacketID() {
		return 1004;
	}

	@Override
	public void handlePacket(Player player, byte[] packet) {
		player.destroy();
	}

}
