package jonquer.packethandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import jonquer.game.Constants;
import jonquer.model.Player;
import jonquer.model.World;
import jonquer.net.StaticPacketBuilder;
import jonquer.util.Formula;
import jonquer.util.Log;
import jonquer.util.Tools;

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
	int id = -1;
	if (p != null) {
	    id = p.getCharacter().getID();
	    player.retainObject(p, key);
	} else {
	    Log.error("Error @ GameLogin 01");
	    player.destroy(true);
	}
	if(id == -1) {
	    Log.error("Error @ GameLogin 02");
	    player.destroy(true);
	}
	
	player.setCharacter(Tools.loadCharacter(player.getCharacter().getAccountName()));
	player.getCharacter().setID(id);

	if(player.getCharacter().getSpouse() == null) {
	    player.getActionSender().sendMessage(0xFFFFFF, Formula.DIALOG_MESSAGE_TYPE, "SYSTEM", "ALLUSERS", "NEW_ROLE");
	} else {
	    Log.debug("Logged In: " + player.getCharacter().getName());
	    player.getActionSender().sendHeroInfo();
	    player.getActionSender().sendMessage(0xFFFFFF, Formula.DIALOG_MESSAGE_TYPE, "SYSTEM", "ALLUSERS", "ANSWER_OK");
	}
    }

}
