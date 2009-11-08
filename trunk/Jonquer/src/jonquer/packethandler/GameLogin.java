package jonquer.packethandler;

import java.util.ArrayList;

import jonquer.debug.Log;
import jonquer.misc.Constants;
import jonquer.misc.Formula;
import jonquer.misc.Tools;
import jonquer.model.GroundItem;
import jonquer.model.Monster;
import jonquer.model.Npc;
import jonquer.model.Player;
import jonquer.model.World;
import jonquer.net.StaticPacketBuilder;
import jonquer.services.IoService;

/**
 * the 'language' packet 0x41c
 * 
 * @author xEnt
 * 
 */
public class GameLogin implements PacketHandler {

    @Override
    public int getPacketID() {
	return 0x41c;
    }

    public void handlePacket(Player player, byte[] packet) throws Exception {
	int pid = StaticPacketBuilder.getInt(packet, 4);
	int key = StaticPacketBuilder.getInt(packet, 8);
	if(player.crypt == null)
	    return;
	player.crypt.generateKeys(pid, key);
	Player p = World.getWorld().getKeyPlayers().get(key);
	int id = -1;
	if (p != null) {
	    id = p.getCharacter().getID();
	    player.retainObject(p, key);
	} else {
	    Log.error("Error @ GameLogin 01");
	    player.destroy(true);
	}
	if(id == -1) {
	    Log.error("Error @ GameLogin 02");
	    player.destroy(true);
	}

	player.setCharacter(IoService.getService().loadCharacter(player.getCharacter().getAccount()));
	player.getCharacter().setNpcsInView(new ArrayList<Npc>());
	player.getCharacter().setMonstersInView(new ArrayList<Monster>());
	player.getCharacter().setItemsInView(new ArrayList<GroundItem>());
	player.getCharacter().setID(id);

	//"We: 
	if(player.getCharacter().getSpouse() == null) {
	    player.getActionSender().sendMessage(0xFFFFFF, Formula.DIALOG_MESSAGE_TYPE, "SYSTEM", "ALLUSERS", "NEW_ROLE");
	} else {
	    Constants.TODAYS_CONNECTIONS++;
	    Constants.PLAYERS_ONLINE++;
	    Log.debug("Logged In: " + player.getCharacter().getName());
	    World.getWorld().getMaps().get(player.getCharacter().getMapid()).addPlayer(player);
	    player.getCharacter().ourPlayer = player;
	    player.getActionSender().sendHeroInfo();
	    player.getActionSender().sendMessage(0xFFFFFF, Formula.DIALOG_MESSAGE_TYPE, "SYSTEM", "ALLUSERS", "ANSWER_OK");
	    player.getActionSender().sendInventory();
	    player.getActionSender().sendEquippedItems();
	    player.getActionSender().sendProfs();
	    player.getActionSender().sendMessage(0xfffff, Formula.TALK_MESSAGE_TYPE, "SYSTEM", "ALL", "Welcome to " + Constants.GAME_NAME + " v" + Constants.VERSION + (Constants.REVISION > 0 ? " (r" + Constants.REVISION + ")" : ""));
	    player.getActionSender().sendMessage(0xfffff, Formula.TALK_MESSAGE_TYPE, "SYSTEM", "ALL", "Players Online: " + Constants.PLAYERS_ONLINE + " Total Connections: " + Constants.TODAYS_CONNECTIONS);
	}
    }

}
