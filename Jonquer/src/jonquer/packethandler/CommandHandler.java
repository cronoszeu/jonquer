package jonquer.packethandler;

import jonquer.model.Player;
import jonquer.model.World;
import jonquer.util.Formula;
import jonquer.util.Log;

public class CommandHandler {

    static String command = "";
    public static void handleCmd(String cmd, Player player) {
	Log.log(cmd);
	String[] args = cmd.split(" ");
	String cmdd = args[0];
	command = cmdd;


	if (Command("/dc", "/disconnect", "/exit")) {
	    player.destroy();
	    return;
	} else if (Command("/who", "/inview")) {
	    System.out.println("Count: " + player.getPlayersInView().size());
	    for (Player p : player.getPlayersInView()) {
		System.out.println(p.getCharacter().getName());
	    }
	} else if (Command("/refresh", "/update")) {
	    player.updateMeToOthers();
	    player.updateOthersToMe();
	    World.getWorld().updatePosition(player);
	} else if (Command("/uplev", "/lvl", "/lv")) {
	    int level = ((byte) Integer.parseInt(args[1])) & 0xff;
	    if (level >= 1 && level <= 150) {
		player.getCharacter().setLevel(level);
		player.getActionSender().sendUpdatePacket(Formula.LEVEL_UPDATE_TYPE, level);
	    }
	} else if (Command("/pro", "/prof")) {
	    player.getCharacter().setProfession((byte) Integer.parseInt(args[1]));
	    player.getActionSender().sendUpdatePacket(Formula.PROFESSION_UPDATE_TYPE,
	    player.getCharacter().getProfession() & 0xff);
	} else if (Command("/cngmap", "/map")) {
	    int map = Integer.parseInt(args[1]);
	    short x = Short.parseShort(args[2]);
	    short y = Short.parseShort(args[3]);
	    player.getCharacter().setMap(map);
	    player.getCharacter().setX(x);
	    player.getCharacter().setY(y);
	    World.getWorld().updatePosition(player);
	    player.getActionSender().sendLocation();
	} else if (Command("/tele", "/fly")) {
	    short x = Short.parseShort(args[1]);
	    short y = Short.parseShort(args[2]);
	    player.getCharacter().setX(x);
	    player.getCharacter().setY(y);
	    player.getActionSender().sendLocation();
	} else if (Command("/broadcast", "/say")) {
	    for (Player p : World.getWorld().getPlayers()) {
		p.getActionSender().sendMessage(0xffffff,Formula.CENTER_MESSAGE_TYPE,"SYSTEM","ALLUSERS", cmd.substring(command.length() + 1));
	    }
	} else if (Command("/saveall")) {
	    for (Player p : World.getWorld().getPlayers()) {
		p.save();
	    }
	} else if (Command("/kickall")) {
	    for (Player p : World.getWorld().getPlayers()) {
		p.destroy();
	    }
	}
    }

    public static boolean Command(String... s) {
	for(String txt : s) {
	    if(txt.equalsIgnoreCase(command))
		return true;
	}
	return false;
    }
}
