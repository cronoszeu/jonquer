package jonquer.packethandler;

import jonquer.debug.Log;
import jonquer.misc.Formula;
import jonquer.misc.StaticData;
import jonquer.misc.Tools;
import jonquer.model.Character;
import jonquer.model.Player;

/**
 * Check and authorize the player to login.
 * 
 * @author xEnt
 * 
 */
public class AuthLogin implements PacketHandler {

    @Override
    public int getPacketID() {
	return 0x41b;
    }

    public void handlePacket(Player player, byte[] packet) throws Exception {
	try {

	    String username = new String(packet, 4, 15).trim();
	    String pass = "";
	    int read = 0x14;
	    while (read < 36 && packet[read] != 0) {
		pass = pass + String.format("%02x", packet[read]);
		read += 1;
	    }
	    if (Tools.accountExists(username)) {
		Log.debug("USER EXISTS! (" + username + ")");
		Character ch = Tools.loadCharacter(username);
		if(ch != null) {
		    if (ch.getPassword().equals(pass)) {
			player.setCharacter(ch);
			login(player);
		    } else {
			Log.debug("Wrong Password for " + username);
			player.destroy();
		    }
		} else {
		    player.getCharacter().setAccount(username);
		    player.getCharacter().setPassword(pass);

		    Tools.saveChar(player);
		    Log.debug("CREATED ACCOUNT: " + username);
		    StaticData.getAccounts().add(username.toLowerCase());
		    login(player);
		}

	    } else {
		player.getCharacter().setAccount(username);
		player.getCharacter().setPassword(pass);

		Tools.saveChar(player);
		Log.debug("CREATED ACCOUNT: " + username);
		StaticData.getAccounts().add(username.toLowerCase());
		login(player);
	    }

	} catch (Exception e) {
	    Log.error(e);
	}
    }

    public void login(Player player) {
	Log.debug("Sucessful Login for " + player.getCharacter().getAccount());
	player.getPacketSender().sendAuthInfo();
    }
}
