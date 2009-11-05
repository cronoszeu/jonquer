package jonquer.packethandler;

import java.nio.ByteBuffer;

import jonquer.debug.Log;
import jonquer.model.Equipment;
import jonquer.model.Inventory;
import jonquer.model.Item;
import jonquer.model.Player;
import jonquer.model.World;
import jonquer.util.Formula;

public class EquipHandler {

    public static void handlePacket(Player player, ByteBuffer bb) {
	try {
	    int itemUID = bb.getInt(4);
	    byte slot = bb.get(8);

	    Item item = null;
	    for(Item i : player.getCharacter().getInventory().getItems()) {
		if(i.getUID() == itemUID) {
		    item = i;
		    break;
		}
	    }

	    if(!checkItem(player, itemUID, slot, item)) {
		player.destroy();
	    }

	    Equipment equips = player.getCharacter().getEquipment();

	    if(item.getDef().isTypeHelmet()) {
		if(equips.getHead() == null) {
		    equips.setHead(item);
		    removeItem(player, item);
		} else {
		    Item old = equips.getHead().clone();
		    equips.setHead(item);
		    removeItem(player, item);
		    addItem(player, old);
		}
		updateAll(player, item, Formula.ARMET_EQUIP_SLOT, true);
		return;
	    }

	    if(item.getDef().isTypeArmor()) {
		if(equips.getArmor() == null) {
		    equips.setArmor(item);
		    removeItem(player, item);
		} else {
		    Item old = equips.getArmor().clone();
		    equips.setHead(item);
		    removeItem(player, item);
		    addItem(player, old);
		}
		updateAll(player, item, Formula.ARMOR_EQUIP_SLOT, true);
	    }

	    if(item.getDef().isTypeNecklace()) {
		if(equips.getNeck() == null) {
		    equips.setNeck(item);
		    removeItem(player, item);
		} else {
		    Item old = equips.getNeck().clone();
		    equips.setNeck(item);
		    removeItem(player, item);
		    addItem(player, old);
		}
		updateAll(player, item, Formula.NECKLACE_BAG_EQUIP_SLOT, true);
	    }

	    if(item.getDef().isTypeRing()) {
		if(equips.getRing() == null) {
		    equips.setRing(item);
		    removeItem(player, item);
		} else {
		    Item old = equips.getRing().clone();
		    equips.setRing(item);
		    removeItem(player, item);
		    addItem(player, old);
		}
		updateAll(player, item, (byte)Formula.RING_EQUIP_SLOT, true);
	    }

	    if(item.getDef().isTypeBoots()) {
		if(equips.getBoots() == null) {
		    equips.setBoots(item);
		    removeItem(player, item);
		} else {
		    Item old = equips.getBoots().clone();
		    equips.setBoots(item);
		    removeItem(player, item);
		    addItem(player, old);
		}
		updateAll(player, item, Formula.BOOT_EQUIP_SLOT, true);
	    }

	    if(item.getDef().isTypeTwoHand()) {
		if(equips.getRight_hand() == null) {
		    equips.setRight_hand(item);
		    removeItem(player, item);
		} else {
		    Item old = equips.getRight_hand().clone();
		    equips.setRight_hand(item);
		    removeItem(player, item);
		    addItem(player, old);
		}
		updateAll(player, item, Formula.RIGHT_WEAPON_EQUIP_SLOT, true);
	    }

	    if(item.getDef().isTypeBow()) {
		if(equips.getRight_hand() != null) {
		    addItem(player, equips.getRight_hand().clone());
		}
		
		if(equips.getLeft_hand() != null && !equips.getLeft_hand().getDef().isArrows()) {
		    addItem(player, equips.getLeft_hand().clone());
		}

		equips.setLeft_hand(item);
		removeItem(player, item);

		updateAll(player, item, Formula.RIGHT_WEAPON_EQUIP_SLOT, true);
	    }

	    // remember to refresh bonuses

	} catch (Exception e) {
	    Log.error(e);
	    player.destroy();
	}
    }


    public static boolean checkItem(Player player, int uid, int slot, Item item) {

	if (!player.getCharacter().getInventory().hasItem(uid)) {
	    Log.hack(player.getCharacter().getName() + " tried to equip an item he did not have");
	    return false;
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

	return true;
    }

    public static void addItem(Player p, Item i) {
	p.getCharacter().getInventory().addItem(i);
	p.getActionSender().sendItem(i);
    }

    public static void removeItem(Player p, Item i) {
	p.getCharacter().getInventory().removeItem(i);
	p.getActionSender().removeItem(i);
    }

    public static void updateAll(Player p, Item i,byte slot, boolean all) {
	p.getActionSender().sendEquippedItem(i, slot);
	if(all) {
	    for(Player pl : World.getWorld().getPlayers()) {
		if(pl.getCharacter().getMapid() == p.getCharacter().getMapid()) {
		    if(Formula.inView(pl.getCharacter(), p.getCharacter())) {
			pl.getActionSender().sendSpawnPacket(p.getCharacter());
		    }
		}
	    }
	}
    }
}
