package jonquer.packethandler;

import jonquer.model.Player;
import jonquer.util.Formula;

public class CharacterCreation implements PacketHandler {

	@Override
	public int getPacketID() {
		return 1001;
	}

	@Override
	public void handlePacket(Player player, byte[] packet) {
		player.getActionSender().sendMessage(0xFFFFFF, Formula.CREATE_ACCOUNT_MESSAGE_TYPE, "SYSTEM", "ALLUSERS", "YOUR BANNED, GTFO " + player.getCharacter().getAccountName());
	}

}
