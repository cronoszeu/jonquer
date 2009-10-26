package jonquer.packethandler;

import jonquer.model.Player;
import jonquer.util.Log;

public class CommandHandler {
    
    public static void handleCmd(String cmd, Player player) {
	Log.log(cmd);
	String[] args = cmd.split(" ");
	String command = args[0];
	
	if(command.equals("/dc")) {
	    player.destroy();
	    return;
	}
    }

}
