package jonquer.packethandler;

import java.nio.ByteBuffer;

import jonquer.debug.Log;
import jonquer.model.Item;
import jonquer.model.Player;
import jonquer.util.Formula;

public class EquipHandler {

    public static void handlePacket(Player player, ByteBuffer bb) {
        try {
            int itemUID = bb.getInt(4);
            byte slot = bb.get(8);
            
            if(!checkItem(player, itemUID, slot)) {
        	player.destroy();
            }
                for (Item item : player.getCharacter().getInventory().getItems()) {
                    if (item.getUID() == itemUID) {
                        player.getActionSender().sendEquippedItem(item, slot);
                    }
                }
            
        } catch (Exception e) {
            Log.error(e);
            player.destroy();
        }
    }
    
    public static boolean checkItem(Player player, int uid, int slot) {
	
	
	if (!player.getCharacter().getInventory().hasItem(uid)) {
            Log.hack(player.getCharacter().getName() + " tried to equip an item he did not have");
            return false;
        } 
	Item item = null;
	for(Item i : player.getCharacter().getInventory().getItems()) {
	    if(i.getUID() == uid) {
		item = i;
		break;
	    }
	}
	if(item == null) {
	    Log.hack(player.getCharacter().getName() + " tried to equip an item he did not have ");
	    return false;
	}
	if(item.getDef().getLevelReq() > player.getCharacter().getLevel()) {
	    Log.hack(player.getCharacter().getName() + " tried to equip an item he does not have the correct level for (" + item.getDef().getName() + ")");
	    return false;
	}
	if(item.getDef().getReqAgility() > player.getCharacter().getAgility() || item.getDef().getReqStrength() > player.getCharacter().getStrength()) {
	    Log.hack(player.getCharacter().getName() + " tried to equip an item he does not have the correct Stats for (" + item.getDef().getName() + ")");
	    return false;
	}
	
	if(item.getDef().isTypeOneHand()) {
	    if(item.getDef().getWeaponType() == Formula.SWORD)
		System.out.println("This weapon is a sword!");
	    if(item.getDef().getWeaponType() == Formula.AXE)
		System.out.println("This weapon is a axe");
	    if(item.getDef().getWeaponType() == Formula.CLUB)
		System.out.println("This weapon is a club");
	    if(item.getDef().getWeaponType() == Formula.BLADE)
		System.out.println("This weapon is a blade!");
	    if(item.getDef().getWeaponType() == Formula.BACKSWORD)
		System.out.println("This weapon is a sword!");
	    if(item.getDef().getWeaponType() == Formula.SCEPTER)
		System.out.println("This weapon is a scepter!");
	}
	
	return true;
    }
}
