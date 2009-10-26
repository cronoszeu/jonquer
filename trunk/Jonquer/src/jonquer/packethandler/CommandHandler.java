package jonquer.packethandler;

import jonquer.model.Player;
import jonquer.model.World;
import jonquer.util.Formula;
import jonquer.util.Log;

public class CommandHandler {

    public static void handleCmd(String cmd, Player player) {
	Log.log(cmd);
	String[] args = cmd.split(" ");
	String command = args[0];

	if(command.equals("/dc")) {
	    player.destroy();
	    return;
	} else if(command.equals("/who")) {
	    System.out.println("Count: " + player.getPlayersInView().size());
	    for(Player p : player.getPlayersInView())
		System.out.println(p.getCharacter().getName());
	} else if(command.equals("/refresh")) {
	   World.getWorld().updatePosition(player);
	} else if(command.equalsIgnoreCase("/uplev")) {
            int level = ((byte) Integer.parseInt(args[1])) & 0xff;
            if(level >= 1 && level <= 150) {
                player.getCharacter().setLevel(level);
                player.getActionSender().sendUpdatePacket(Formula.LEVEL_UPDATE_TYPE,
                    level);
            }
        } else if(command.equalsIgnoreCase("/pro")) {
            player.getCharacter().setProfession((byte) Integer.parseInt(args[1]));
            player.getActionSender().sendUpdatePacket(Formula.PROFESSION_UPDATE_TYPE,
                    player.getCharacter().getProfession() & 0xff);
        }
    }

}