package jonquer.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import jonquer.model.Character;
import jonquer.debug.Log;
import jonquer.listeners.CharacterIoListener;
import jonquer.listeners.ServerIoListener;
import jonquer.misc.Constants;
import jonquer.misc.ServerSettings;

/**
 * This class handles all local input/output
 * also has capabilities to load/save to multiple formats, as long as the plugins are provided.
 * @author xEnt
 *
 */
public class IoService {

    private IoService() { }

    private CharacterIoListener charListener = null;
    private ServerIoListener serverListener = null;

    private static IoService io;

    public static String characterIOType = null;
    public static String serverIOType = null;

    public static IoService getService() {
	if(io == null)
	    io = new IoService();
	return io;
    }

    public Character loadCharacter(String username) {
	return charListener.loadCharacter(username);
    }

    public ServerSettings loadServerSettings() {
	return serverListener.loadServerSettings();
    }

    public void saveCharacter(Character character) {
	charListener.saveCharacter(character);
    }

    public void saveServerSettings(ServerSettings settings) {
	serverListener.saveServerSettings(settings);
    }


    @SuppressWarnings("unchecked")
    public void initIoPluggables() {
	for(File f : new File(Constants.USER_DIR + "/src/jonquer/plugins/io/").listFiles()) {
	    if(f.getName().endsWith(".java")) {
		try {
		    try {
			Class c = Class.forName("jonquer.plugins.io." + f.getName().replace(".java", ""));
			Object o = c.newInstance();

			if(o instanceof CharacterIoListener) {
			    CharacterIoListener list = (CharacterIoListener)o;
			    characterIOType = list.getIoName();
			    charListener = list;
			} else if(o instanceof ServerIoListener) {
			    ServerIoListener list = (ServerIoListener)o;
			    serverIOType = list.getIoName();
			    serverListener = list;
			}

		    } catch (InstantiationException e) {
			e.printStackTrace();
		    } catch (IllegalAccessException e) {
			e.printStackTrace();
		    }
		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		}

	    }
	}
    }


    public static final int IO_CHARACTER = 1;
    public static final int IO_SERVER = 2;

}
