package jonquer.packethandler;

import java.nio.ByteBuffer;

import jonquer.debug.Log;
import jonquer.model.Player;

public class EquipHandler {

    public static void handlePacket(Player player, ByteBuffer bb) {
	try {
	    int itemUID = bb.getInt(4);
	    if(!player.getCharacter().getInventory().hasItem(itemUID)) {
		Log.hack(player.getCharacter().getName() + " tried to equip an item he did not have");
		return;
	    }
	} catch(Exception e) {
	    Log.error(e);
	    player.destroy();
	}
    }

}
