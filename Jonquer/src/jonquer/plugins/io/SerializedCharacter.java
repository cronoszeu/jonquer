package jonquer.plugins.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import jonquer.debug.Log;
import jonquer.listeners.CharacterIoListener;
import jonquer.misc.Constants;
import jonquer.misc.Formula;
import jonquer.model.Character;

/**
 * This class is a plugin for using the Serialization approach to saving/loading character data.
 * @author xEnt
 *
 */
public class SerializedCharacter implements CharacterIoListener {

    @Override
    public String getIoName() {
	return "Serialiazion";
    }
 
    @Override
    public Character loadCharacter(String username) {
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

    @Override
    public void saveCharacter(Character character) {
	try {
	    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Constants.SAVED_GAME_DIRECTORY + character.getAccount().toLowerCase() + ".cfg"));
	    oos.writeObject(character);
	    oos.close();
	} catch(Exception e) {
	    Log.error(e);
	}
    }

}
