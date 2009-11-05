package jonquer.packethandler;

import jonquer.debug.Log;
import jonquer.model.Item;
import jonquer.model.Player;
import jonquer.model.World;
import jonquer.model.def.COItemDef;
import jonquer.util.Formula;
import jonquer.util.StaticData;

public class CommandHandler {

    static String command = "";
    public static void handleCmd(String cmd, Player player) {
	
	Log.log(cmd);
	String[] args = cmd.split(" ");
	String cmdd = args[0];
	command = cmdd;

	if(command("/blocked")) {
	    if(Formula.isTileBlocked(player.getCharacter().getMapid(), Integer.parseInt(args[1]), Integer.parseInt(args[2]))) {
		player.getActionSender().sendSystemMessage("Yeah, blocked");
	    } else {
		player.getActionSender().sendSystemMessage("Nope, not blocked");
	    }
		
	}
	if (command("/dc", "/disconnect", "/exit")) {
	    player.destroy();
	    return;
	} else if (command("/who", "/inview")) {
	    System.out.println("Count: " + player.getPlayersInView().size());
	    for (Player p : player.getPlayersInView()) {
		System.out.println(p.getCharacter().getName());
	    }
	} else if (command("/refresh", "/update")) {
	    player.updateMeToOthers();
	    player.updateOthersToMe();
	  //  World.getWorld().updatePosition(player);
	} else if (command("/uplev", "/lvl", "/lv")) {
	    int level = ((byte) Integer.parseInt(args[1])) & 0xff;
	    if (level >= 1 && level <= 150) {
		player.getCharacter().setLevel(level);
		player.getActionSender().sendUpdatePacket(Formula.LEVEL_UPDATE_TYPE, level);
	    }
	} else if (command("/pro", "/prof")) {
	    player.getCharacter().setProfession((byte) Integer.parseInt(args[1]));
	    player.getActionSender().sendUpdatePacket(Formula.PROFESSION_UPDATE_TYPE,
		    player.getCharacter().getProfession() & 0xff);
	} else if(command("/test1")) {
	    for(Player p : World.getWorld().getPlayers()) {
                if(p != player)
                if(p.getCharacter().getMapid() == player.getCharacter().getMapid()) {
                    p.getActionSender().sendSpawnPacket(player.getCharacter());
                }
	    }
	} else if(command("/test2")) {
	    for(Player p : World.getWorld().getPlayers()) {
                if(p != player)
                if(p.getCharacter().getMapid() == player.getCharacter().getMapid()) {
                    player.getActionSender().sendSpawnPacket(p.getCharacter());
                }
	    }
	}
	 else if(command("/test3")) {
		    for(Player p : World.getWorld().getPlayers()) {
	                if(p != player)
	                if(p.getCharacter().getMapid() == player.getCharacter().getMapid()) {
	                    player.getActionSender().removeEntity(p);
	                    player.getActionSender().sendSpawnPacket(p.getCharacter());
	                }
		    }
		}
	
	else if (command("/chngmap", "/map")) {
	    int map = Integer.parseInt(args[1]);
	    short x = Short.parseShort(args[2]);
	    short y = Short.parseShort(args[3]);
	    player.getCharacter().setMapid(map);
	    player.getCharacter().setX(x);
	    player.getCharacter().setY(y);
	    //World.getWorld().updatePosition(player);
	    player.getActionSender().sendLocation();
	} else if (command("/tele", "/fly")) {
	    short x = Short.parseShort(args[1]);
	    short y = Short.parseShort(args[2]);
	    player.getCharacter().setX(x);
	    player.getCharacter().setY(y);
	    player.getActionSender().sendLocation();
	} else if (command("/broadcast", "/say")) {
	    for (Player p : World.getWorld().getPlayers()) {
		p.getActionSender().sendMessage(0xffffff,Formula.CENTER_MESSAGE_TYPE,"SYSTEM","ALLUSERS", cmd.substring(command.length() + 1));
	    }
	} else if (command("/saveall")) {
	    for (Player p : World.getWorld().getPlayers()) {
		p.save();
	    }
	} else if (command("/kickall")) {
	    for (Player p : World.getWorld().getPlayers()) {
		p.destroy();
	    }
	} else if (command("/mob")) {
	    int id = Integer.parseInt(args[1]);
	    int mesh = Integer.parseInt(args[2]);
	    int x = Integer.parseInt(args[3]);
	    int y = Integer.parseInt(args[4]);
	    String name = args[5];
	    int health = Integer.parseInt(args[6]);
	    int level = Integer.parseInt(args[7]);
	    int pos = Integer.parseInt(args[8]);
	  //  player.getActionSender().sendMonsterSpawn(id, mesh, x, y, name, health, level, pos);
	} else if (command("/npc")) {
	    int id = Integer.parseInt(args[1]);
	    int x = Integer.parseInt(args[2]);
	    int y = Integer.parseInt(args[3]);
	    int type = Integer.parseInt(args[4]);
	    int direction = Integer.parseInt(args[5]);
	    int flag = Integer.parseInt(args[6]);
	    player.getActionSender().sendNpcSpawn(id, x, y, type, direction, flag);
	} else if(command("/item")) {
	    player.getCharacter().getInventory().addItem(new Item(Integer.parseInt(args[1]), 0, 0, 0, 0, 0));
	    player.getActionSender().sendInventory();
	}
    }

    public static boolean command(String... s) {
	for(String txt : s) {
	    if(txt.equalsIgnoreCase(command))
		return true;
	}
	return false;
    }
}
