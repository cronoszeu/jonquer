package jonquer.packethandler;

import java.awt.Point;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import jonquer.debug.Log;
import jonquer.misc.Formula;
import jonquer.model.GroundItem;
import jonquer.model.Item;
import jonquer.model.Player;
import jonquer.model.World;
import jonquer.model.future.Timer;

public class FrameHandler implements PacketHandler {

    @Override
    public int getPacketID() {
	return 1009;
    }

    @Override
    public void handlePacket(Player player, byte[] packet) {

	ByteBuffer bb = ByteBuffer.wrap(packet);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	int id = bb.getInt(0x0c);
	if(id == 4) {
	    EquipHandler.handlePacket(player, bb);
	    return;
	}

	switch(id) {

	case 3:
	    int uid = bb.getInt(4);
	    if(player.getCharacter().getInventory().hasItem(uid))  {
		Item i = player.getCharacter().getInventory().getItem(uid);
		Point valid = Formula.validDropTile(player.getCharacter().getX(), player.getCharacter().getY(), player.getCharacter().getMapid());
		if(valid == null)
		    return;
		int map = player.getCharacter().getMapid();
		final GroundItem gi = new GroundItem(i.getUID(), i.getID(), i.getPlus(), i.getBless(), i.getEnchant(), i.getSoc1(), i.getSoc2(), (int)valid.getX(), (int)valid.getY(), map);
		player.getMap().addGroundItem(gi, valid, map);
		player.getCharacter().getInventory().removeItem(i);
		player.getActionSender().removeItem(i);
		World.getWorld().getTimerService().add(new Timer(8000, null) {
		    public void execute() {
			if(gi != null) {
			    World.getWorld().getMaps().get(gi.getMap()).getGroundItems().remove(gi);
			    for(Player p : World.getWorld().getMaps().get(gi.getMap()).getPlayers().values()) {
				if(Formula.inView(gi.getX(), gi.getY(), p.getCharacter().getX(), p.getCharacter().getY())) {
				    p.getCharacter().getItemsInView().remove(gi);
				    p.getActionSender().removeGroundItem(gi);
				}
			    }
			}
		    }
		});

	    }
	    break;

	case 6:

	    int itemUID = bb.getInt(4);
	    byte slot = bb.get(8);
	    Item item = null;
	    switch (slot) {
	    case Formula.ARMET_EQUIP_SLOT:
		if (player.getCharacter().getEquipment().
			getHead().getUID() == itemUID) {
		    item = player.getCharacter().getEquipment().getHead();
		    player.getCharacter().getEquipment().setHead(null);
		}
		break;

	    case Formula.NECKLACE_BAG_EQUIP_SLOT:
		if (player.getCharacter().getEquipment().
			getNeck().getUID() == itemUID) {
		    item = player.getCharacter().getEquipment().getNeck();
		    player.getCharacter().getEquipment().setNeck(null);
		}
		break;

	    case Formula.ARMOR_EQUIP_SLOT:
		if (player.getCharacter().getEquipment().
			getArmor().getUID() == itemUID) {
		    item = player.getCharacter().getEquipment().getArmor();
		    player.getCharacter().getEquipment().setArmor(null);
		}
		break;

	    case Formula.RIGHT_WEAPON_EQUIP_SLOT:
		if (player.getCharacter().getEquipment().
			getRight_hand().getUID() == itemUID) {
		    item = player.getCharacter().getEquipment().getRight_hand();
		    player.getCharacter().getEquipment().setRight_hand(null);
		}
		break;

	    case Formula.LEFT_WEAPON_EQUIP_SLOT:
		if (player.getCharacter().getEquipment().
			getLeft_hand().getUID() == itemUID) {
		    item = player.getCharacter().getEquipment().getLeft_hand();
		    player.getCharacter().getEquipment().setLeft_hand(null);
		}
		break;

	    case Formula.RING_EQUIP_SLOT:
		if (player.getCharacter().getEquipment().
			getRing().getUID() == itemUID) {
		    item = player.getCharacter().getEquipment().getRing();
		    player.getCharacter().getEquipment().setRing(null);
		}
		break;

		//                case Formula.TALISMAN_EQUIP_SLOT:
		    //                    if(player.getCharacter().getEquipment().
			    //                            getTalisman().getUID() == itemUID) {
			//                        item = player.getCharacter().getEquipment().getTalisman();
			//                        player.getCharacter().getEquipment().setTalisman(null);
			//                    }
		    //                    break;

		case Formula.BOOT_EQUIP_SLOT:
		    if (player.getCharacter().getEquipment().
			    getBoots().getUID() == itemUID) {
			item = player.getCharacter().getEquipment().getBoots();
			player.getCharacter().getEquipment().setBoots(null);
		    }
		    break;

		    //                case Formula.GARMENT_EQUIP_SLOT:
			//                    if(player.getCharacter().getEquipment().
		    //                            getGarment().getUID() == itemUID) {
		    //                        item = player.getCharacter().getEquipment().getGarment();
		    //                        player.getCharacter().getEquipment().setGarment(null);
		    //                    }
		    //                    break;
	    }
	    if (item != null) {
		player.getActionSender().sendUnequipItem(item, slot);
		player.getCharacter().getInventory().addItem(item);
		player.getActionSender().sendItem(item);
		for (Player p : player.getMap().getPlayers().values()) {
		    if (p.getCharacter().inview(player.getCharacter())) {
			p.getActionSender().sendSpawnPacket(player.getCharacter());
		    }
		}
	    } else {
		Log.hack("Trying to unequip an item that the player did not have.");
	    }
	    return;

	default:
	    System.out.println("Frame handler: ID " + id);
	}


    }

}
