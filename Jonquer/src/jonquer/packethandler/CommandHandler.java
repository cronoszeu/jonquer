package jonquer.packethandler;

import jonquer.model.Player;
import jonquer.model.World;
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

	if(command.equals("/who")) {
	    System.out.println("Count: " + player.getPlayersInView().size());
	    for(Player p : player.getPlayersInView())
		System.out.println(p.getCharacter().getName());
	}
	if(command.equals("/refresh")) {
	   World.getWorld().updatePosition(player);
	}
    }

}
