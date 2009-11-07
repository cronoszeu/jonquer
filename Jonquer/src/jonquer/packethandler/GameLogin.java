package jonquer.packethandler;

import java.util.ArrayList;

import jonquer.debug.Log;
import jonquer.misc.Formula;
import jonquer.misc.Tools;
import jonquer.model.GroundItem;
import jonquer.model.Monster;
import jonquer.model.Npc;
import jonquer.model.Player;
import jonquer.model.World;
import jonquer.net.StaticPacketBuilder;

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

	player.setCharacter(Tools.loadCharacter(player.getCharacter().getAccount()));
	player.getCharacter().setNpcsInView(new ArrayList<Npc>());
	player.getCharacter().setMonstersInView(new ArrayList<Monster>());
	player.getCharacter().setItemsInView(new ArrayList<GroundItem>());
	player.getCharacter().setID(id);

	//"We: 
	if(player.getCharacter().getSpouse() == null) {
	    player.getActionSender().sendMessage(0xFFFFFF, Formula.DIALOG_MESSAGE_TYPE, "SYSTEM", "ALLUSERS", "NEW_ROLE");
	} else {
	    Log.debug("Logged In: " + player.getCharacter().getName());
	    World.getWorld().getMaps().get(player.getCharacter().getMapid()).addPlayer(player);
	    player.getCharacter().ourPlayer = player;
	    player.getActionSender().sendHeroInfo();
	    player.getActionSender().sendMessage(0xFFFFFF, Formula.DIALOG_MESSAGE_TYPE, "SYSTEM", "ALLUSERS", "ANSWER_OK");
	    player.getActionSender().sendInventory();
	    player.getActionSender().sendEquippedItems();
	}
    }

}
