package jonquer.packethandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import jonquer.game.Constants;
import jonquer.model.Player;
import jonquer.util.Log;
import jonquer.util.Tools;

import org.apache.mina.common.ByteBuffer;

/**
 * Check and authorize the player to login. Password MUST be 6 characters long
 * when registering (at least should be)
 * 
 * @author xEnt
 * 
 */
public class AuthLogin implements PacketHandler {

    @Override
    public int getPacketID() {
	return 0x41b;
    }

    public void handlePacket(Player player, byte[] packet) {
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
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(Constants.SAVED_GAME_DIRECTORY + username + ".cfg")));
		jonquer.model.Character ch = (jonquer.model.Character) in.readObject();
		in.close();
		if (ch.getPassword().equals(pass)) {
		    login(player);
		} else {
		    Log.debug("Wrong Password for " + username);
		    player.destroy();
		}

	    } else {
		player.getCharacter().setAccountName(username);
		player.getCharacter().setPassword(pass);
		player.getCharacter().setConquerPoints(0);
		player.getCharacter().setDexterity((short)50);
		player.getCharacter().setHairstyle((short)130);
		player.getCharacter().setHealthPoints((short)1000);
		player.getCharacter().setLevel((byte)1);
		player.getCharacter().setManaPoints((short)1000);
		player.getCharacter().setMap(1002);
		player.getCharacter().setModel(1003);
		player.getCharacter().setMoney(0);
		player.getCharacter().setName("Test");
		player.getCharacter().setProfession((byte)15);
		player.getCharacter().setReborn((byte)0);
		player.getCharacter().setSpirit((short)5);
		player.getCharacter().setSpouse("None");
		player.getCharacter().setStats((short)20);
		player.getCharacter().setStrength((short)50);
		player.getCharacter().setVitality((short)50);
		player.getCharacter().setX((short)400);
		player.getCharacter().setY((short)400);
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Constants.SAVED_GAME_DIRECTORY + username + ".cfg"));
		oos.writeObject(player.getCharacter());
		oos.close();
		Log.debug("CREATED ACCOUNT: " + username);
		login(player);
	    }

	} catch (Exception e) {
	    Log.error(e);
	}
    }

    public void login(Player player) {
	Log.debug("Sucessful Login for " + player.getCharacter().getAccountName());
	player.getPacketSender().sendAuthInfo();
    }
}
