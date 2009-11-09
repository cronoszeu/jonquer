package jonquer.plugins.io;

import jonquer.listeners.ServerIoListener;
import jonquer.misc.ServerSettings;

public class INIServerSettings implements ServerIoListener {

    @Override
    public String getIoName() {
	return "Unfinished..";
    }

    @Override
    public ServerSettings loadServerSettings() {
	return null;
    }
 
    @Override
    public void saveServerSettings(ServerSettings settings) {
	
    }

}
