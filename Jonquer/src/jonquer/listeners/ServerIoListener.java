package jonquer.listeners;

import jonquer.misc.ServerSettings;

/**
 * 
 * @author xEnt
 *
 */
public interface ServerIoListener {
    
    public ServerSettings loadServerSettings();
    
    public void saveServerSettings(ServerSettings settings);
    
    public String getIoName();

}
