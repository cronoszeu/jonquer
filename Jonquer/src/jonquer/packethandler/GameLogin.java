package jonquer.packethandler;

import jonquer.model.Player;
import jonquer.model.World;
import jonquer.net.StaticPacketBuilder;
import jonquer.util.Formula;

/**
 * the 'language' packet 0x41c
 * 
 * @author xEnt
 * 
 */
public class GameLogin implements PacketHandler {

	@Override
	public int getPacketID() {
		return 0x41c;
	}

	public void handlePacket(Player player, byte[] packet) {
		int pid = StaticPacketBuilder.getInt(packet, 4);
		int key = StaticPacketBuilder.getInt(packet, 8);
		player.crypt.generateKeys(pid, key);
		Player p = World.getWorld().getKeyPlayers().get(key);
		if (p != null) {
			player.retainObject(p, key);
		}
		
		//if(player.getCharacter().getProfession() == (byte)0) {
		//	player.getActionSender().sendMessage(0xFFFFFF, Formula.DIALOG_MESSAGE_TYPE, "SYSTEM", "ALLUSERS", "NEW_ROLE");
		//} else {
			player.getActionSender().sendHeroInfo();
			player.getActionSender().sendMessage(0xFFFFFF, Formula.DIALOG_MESSAGE_TYPE, "SYSTEM", "ALLUSERS", "ANSWER_OK");
		//}
		//player.getActionSender().sendHeroInfo();
	

	}

}
