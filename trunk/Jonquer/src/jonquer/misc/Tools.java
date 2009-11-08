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

	for(String ss : StaticData.getAccounts()) {
	    if(s.toLowerCase().equals(ss.toLowerCase()))
		return true;
	}
	return false;
    }

}
