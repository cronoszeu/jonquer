package jonquer.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import jonquer.debug.Log;
import jonquer.model.Player;
import jonquer.model.Character;

public class Tools {

    /**
     * Checks if an account name is in use or not.
     * 
     * @param s
     *            - the character name
     * @return - if the account exists or not
     */
    public static boolean accountExists(String s) {
	// debugging an error i randomly get
	System.out.println("We: " + s);
	for(String ss : StaticData.getAccounts()) {
	    System.out.println(ss);
	    if(s.toLowerCase().equals(ss.toLowerCase()))
		return true;
	}
	return false;
    }

    /**
     * Saves a Character to file
     * @param player
     */
    public static void saveChar(Player player) {
	ObjectOutputStream oos;
	try {
	    oos = new ObjectOutputStream(new FileOutputStream(Constants.SAVED_GAME_DIRECTORY + player.getCharacter().getAccount().toLowerCase() + ".cfg"));
	    oos.writeObject(player.getCharacter());
	    oos.close();
	} catch(Exception e) {
	    Log.error(e);
	}
    }

    /**
     * Loads a Players Character Profile
     * @param username - the username of the account
     * @return - the Character object
     */
    public static Character loadCharacter(String username) {
	try {
	    ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(Constants.SAVED_GAME_DIRECTORY + username.toLowerCase() + ".cfg")));
	    Character ch = (Character) in.readObject();
	    in.close();
	    return ch;
	} catch(Exception e) {
	    Log.error(e);
	}
	return null;
    }

}
