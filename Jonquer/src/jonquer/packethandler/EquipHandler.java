package jonquer.packethandler;

import java.nio.ByteBuffer;

import jonquer.debug.Log;
import jonquer.model.Item;
import jonquer.model.Player;

public class EquipHandler {

    public static void handlePacket(Player player, ByteBuffer bb) {
        try {
            int itemUID = bb.getInt(4);
            byte slot = bb.get(8);
            if (!player.getCharacter().getInventory().hasItem(itemUID)) {
                Log.hack(player.getCharacter().getName() + " tried to equip an item he did not have");
            } else {
                for (Item item : player.getCharacter().getInventory().getItems()) {
                    if (item.getUID() == itemUID) {
                        player.getActionSender().sendEquippedItem(item, slot);
                    }
                }
            }
        } catch (Exception e) {
            Log.error(e);
            player.destroy();
        }
    }
}
