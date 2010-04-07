package jonquer.packethandler;

import java.nio.ByteBuffer;

import jonquer.debug.Log;
import jonquer.misc.Formula;
import jonquer.misc.StaticData;
import jonquer.model.Equipment;
import jonquer.model.Inventory;
import jonquer.model.Item;
import jonquer.model.Player;
import jonquer.model.World;
import jonquer.model.def.COItemDef;
import jonquer.plugins.skills.Rage;

public class EquipHandler {

    public static void handlePacket(Player player, ByteBuffer bb) {
	try {
	    int itemUID = bb.getInt(4);
	    byte slot = bb.get(8);

	    Item ii = player.getCharacter().getInventory().getItem(itemUID);
	    if(ii != null)
		System.out.println(ii.getDef().getID());
	    else
		System.out.println("equip handler null?");
	    

	    if(slot == 0) { // conjurable item
		useItem(player, ii.getDef().getID(), ii);
		return;
	    }

	    Item item = null;
	    for(Item i : player.getCharacter().getInventory().getItems()) {
		if(i.getUID() == itemUID) {
		    item = i;
		    break;
		}
	    }

	    if(!checkItem(player, itemUID, slot, item)) {
		return;
	    }
	    if(item == null)
		return;

	    Equipment equips = player.getCharacter().getEquipment();

	    if(item.getDef().isTypeHelmet()) {
		if(equips.getHead() == null) {
		    equips.setHead(item);
		    removeItem(player, item);
		} else {
		    Item old = equips.getHead().clone();
		    unequipItem(player, old, slot);
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
		    unequipItem(player, old, slot);
		    equips.setHead(item);
		    removeItem(player, item);
		    addItem(player, old);
		}
		updateAll(player, item, Formula.ARMOR_EQUIP_SLOT, true);
	    }

	    if(item.getDef().isTypeNecklace()) {
		if(equips.getNeck() == null) {
		    // unequipItem(player, item, slot);
		    Item i = item.clone();
		    equips.setNeck(item.clone());
		    removeItem(player, item);
		    equipItem(player, i, slot);
		} else {
		    Item old = equips.getNeck().clone();
		    unequipItem(player, old, slot);
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
		    unequipItem(player, old, slot);
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
		    unequipItem(player, old, slot);
		    equips.setBoots(item);
		    removeItem(player, item);
		    addItem(player, old);
		}
		updateAll(player, item, Formula.BOOT_EQUIP_SLOT, true);
	    }
	    if(slot == 5) {
		if(equips.getRight_hand() == null) {
		    player.getActionSender().sendSystemMessage("Please equip your right hand first");
		    return;
		} 
		if(equips.getRight_hand().getDef().isTypeTwoHand()) {
		    return;
		} 

		if(equips.getLeft_hand() != null) {
		    Item old = equips.getLeft_hand().clone();
		    unequipItem(player, old, slot);
		    equips.setLeft_hand(item);
		    removeItem(player, item);
		    addItem(player, old);
		    updateAll(player, item, Formula.LEFT_WEAPON_EQUIP_SLOT, true);
		} else {
		    equips.setLeft_hand(item);
		    removeItem(player, item);
		    updateAll(player, item, Formula.LEFT_WEAPON_EQUIP_SLOT, true);
		}
		return;

	    }

	    if(item.getDef().isArrows()) {
		if(equips.getRight_hand() != null && equips.getRight_hand().getDef().isTypeBow()) {
		    if(equips.getLeft_hand() != null) {

			Item old = equips.getLeft_hand().clone();
			unequipItem(player, old, slot);
			equips.setLeft_hand(item);
			removeItem(player, item);
			addItem(player, old);
		    } else {
			equips.setLeft_hand(item);
			removeItem(player, item);
		    }
		    updateAll(player, item, Formula.LEFT_WEAPON_EQUIP_SLOT, true);
		}
	    }

	    if(item.getDef().isTypeBow()) {
		if(equips.getRight_hand() != null) {
		    Item i = equips.getRight_hand().clone();
		    unequipItem(player, i, slot);
		    addItem(player, i);
		}

		/*if(equips.getLeft_hand() != null && !equips.getLeft_hand().getDef().isArrows()) {
		    Item i = equips.getLeft_hand().clone();
		    unequipItem(player, i, slot);
		    addItem(player, i);
		}

		equips.setLeft_hand(item);
		removeItem(player, item);*/

		updateAll(player, item, Formula.RIGHT_WEAPON_EQUIP_SLOT, true);
	    }

	    if(item.getDef().isTypeTwoHand()) {
		if(checkAndRemoveWeapons(equips, player, item, slot)) {
		    updateAll(player, item, Formula.RIGHT_WEAPON_EQUIP_SLOT, true);
		}
	    } else if(item.getDef().isTypeOneHand()) {
		if(checkAndRemoveWeapons(equips, player, item, slot)) {
		    updateAll(player, item, Formula.RIGHT_WEAPON_EQUIP_SLOT, true);
		}
	    }

	    // remember to refresh bonuses

	} catch (Exception e) {
	    Log.error(e);
	    player.destroy();
	}
    }

    public static boolean checkAndRemoveWeapons(Equipment equips, Player player, Item newitem, byte slot) {
	if(newitem.getDef().isTypeTwoHand() && !newitem.getDef().isTypeBow()) {
	    if(equips.getLeft_hand() != null) {
		player.getActionSender().sendSystemMessage("You must un-equip your left-hand first");
		return false;
	    } else {
		if(equips.getRight_hand() != null) {
		    Item i = equips.getRight_hand().clone();
		    unequipItem(player, i, slot);
		    removeItem(player, i);
		    addItem(player, i);
		}
		equips.setRight_hand(newitem);
		removeItem(player, newitem);
		return true;
	    }
	} else if(newitem.getDef().isTypeOneHand()) {
	    if(equips.getRight_hand() != null) {
		Item i = equips.getRight_hand().clone();
		unequipItem(player, i, slot);
		removeItem(player, i);
		addItem(player, i);
	    }
	    equips.setRight_hand(newitem);
	    removeItem(player, newitem);
	    return true;
	}

	return false;
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

    public static void equipItem(Player p, Item i, byte slot) {
	p.getActionSender().sendItem(i, slot);
    }

    public static void unequipItem(Player p, Item i, byte slot) {
	p.getActionSender().sendUnequipItem(i, slot);
    }

    public static void removeItem(Player p, Item i) {
	p.getCharacter().getInventory().removeItem(i);
	p.getActionSender().removeItem(i);
    }

    public static void updateAll(Player p, Item i,byte slot, boolean all) {
	p.updateBaseDamages();
	p.getActionSender().sendEquippedItem(i, slot);
	if(all) {
	    for(Player pl : p.getMap().getPlayers().values()) {
		if(Formula.inView(pl.getCharacter(), p.getCharacter())) {
		    pl.getActionSender().sendSpawnPacket(p.getCharacter());
		}

	    }
	}
    }
    
    public static void useItem(Player player, int id, Item i) {
	COItemDef item = StaticData.itemDefs.get(id);
	if(item != null) {
	    if(item.isHealthPotion()) {
		int hp = player.getCurHP() + item.getPotionRecovery();
		if(hp > player.getMaxHealth())
		    hp = player.getMaxHealth();
		player.setCurHP(hp);
		player.getCharacter().getInventory().removeItem(i);
		player.getActionSender().removeItem(i);
	    } else if(item.isManaPotion()) {
		int mp = player.getCharacter().getMana() + item.getPotionRecovery();
		if(mp > player.getCharacter().getSpirit() * Formula.SPIRIT_TO_MANA_MULTIPLIER)
		    mp = player.getCharacter().getSpirit() * Formula.SPIRIT_TO_MANA_MULTIPLIER;
		player.getCharacter().setMana((short)mp);
		player.getActionSender().vital(player.getUID(), 2, player.getCharacter().getMana());
		player.getCharacter().getInventory().removeItem(i);
		player.getActionSender().removeItem(i);
	    } else {
		System.out.println("Unknown usuable?? " + id);
	    }
	}
    }
}
