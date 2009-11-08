package jonquer.packethandler;

import java.awt.Point;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import java.util.HashSet;
import jonquer.debug.Log;
import jonquer.misc.Constants;
import jonquer.misc.Formula;
import jonquer.misc.StaticData;
import jonquer.model.Character;
import jonquer.model.GroundItem;
import jonquer.model.Item;
import jonquer.model.Npc;
import jonquer.model.Player;
import jonquer.model.Shop;
import jonquer.model.World;
import jonquer.model.def.COItemDef;
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
	if (id == 4) {
	    EquipHandler.handlePacket(player, bb);
	    return;
	}

	switch (id) {
	case 1:
	    int item_id = bb.getInt(8);
	    int timestamp = bb.getInt(16);
	    short zero1 = bb.getShort(20);
	    short zero2 = bb.getShort(22);
	    search:
		for (Shop shop : StaticData.shops.values()) {
		    for (Npc npc : World.getWorld().getNpcs()) {
			if (shop.getId() == npc.getId()) {
			    if (player.getCharacter().getMapid() == npc.getMapid() &&
				    Formula.distance(player.getCharacter().getX(), player.getCharacter().getY(), npc.getCellx(), npc.getCelly()) <= Character.VIEW_RANGE) {
				if (shop.hasItem(item_id)) {
				    if (player.getCharacter().getInventory().canHold(1)) {
					COItemDef itemDef = StaticData.itemDefs.get(item_id);
					if (player.getCharacter().getMoney() >= itemDef.getPrice()) {
					    Item item = new Item(item_id, 0, 0, 0, 0, 0);
					    item.getDef().getPrice();
					    player.getCharacter().getInventory().addItem(item);
					    player.getCharacter().setMoney(player.getCharacter().getMoney() - itemDef.getPrice());
					    player.getActionSender().sendItem(item);
					    player.getActionSender().sendMoney();
					    break search;
					} else {
					    player.getActionSender().sendSystemMessage("You don't have enough money");
					}
				    } else {
					player.getActionSender().sendSystemMessage("You don't have enough free bag space");
				    }
				}
			    }
			}
		    }
		}
	    break;

	case 2:
	    item_id = bb.getInt(8);
	    timestamp = bb.getInt(16);
	    zero1 = bb.getShort(20);
	    zero2 = bb.getShort(22);
	    for (Npc npc : World.getWorld().getNpcs()) {
		if (npc.getType() == 1 &&
			npc.getMapid() == player.getCharacter().getMapid() &&
			Formula.distance(player.getCharacter().getX(), player.getCharacter().getY(), npc.getCellx(), npc.getCelly()) <= Character.VIEW_RANGE) {
		    if (player.getCharacter().getInventory().hasItem(item_id)) {
			Item item = player.getCharacter().getInventory().getItem(item_id);
			player.getCharacter().setMoney((int) (player.getCharacter().getMoney() + (double) item.getDef().getPrice() / 3));
			player.getCharacter().getInventory().removeItem(item);
			player.getActionSender().removeItem(item);
			player.getActionSender().sendMoney();
		    }
		}
	    }
	    break;

	case 3:
	    int uid = bb.getInt(4);
	    if (player.getCharacter().getInventory().hasItem(uid)) {
		Item i = player.getCharacter().getInventory().getItem(uid);
		Point valid = Formula.validDropTile(player.getCharacter().getX(), player.getCharacter().getY(), player.getCharacter().getMapid());
		if (valid == null) {
		    return;
		}
		int map = player.getCharacter().getMapid();
		final GroundItem gi = new GroundItem(i.getUID(), i.getID(), i.getPlus(), i.getBless(), i.getEnchant(), i.getSoc1(), i.getSoc2(), (int) valid.getX(), (int) valid.getY(), map);
		player.getMap().addGroundItem(gi, valid, map);
		player.getCharacter().getInventory().removeItem(i);
		player.getActionSender().removeItem(i);
		World.getWorld().getTimerService().add(new Timer(90000, null) {

		    public void execute() {
			if (gi != null) {
			    World.getWorld().getMaps().get(gi.getMap()).getGroundItems().remove(gi);
			    for (Player p : World.getWorld().getMaps().get(gi.getMap()).getPlayers().values()) {
				if (Formula.inView(gi.getX(), gi.getY(), p.getCharacter().getX(), p.getCharacter().getY())) {
				    p.getCharacter().getItemsInView().remove(gi);
				    p.getActionSender().removeGroundItem(gi);

				}
			    }
			}
		    }
		});

	    }
	    break;

	case 12:


	    int amount = bb.getInt(4);
	    if (player.getCharacter().getMoney() < 100) {
		return;
	    }
	    if (player.getCharacter().getMoney() < amount) {
		player.getActionSender().sendSystemMessage("You don't have that much money");
		return;
	    }
	    Point valid = Formula.validDropTile(player.getCharacter().getX(), player.getCharacter().getY(), player.getCharacter().getMapid());
	    if (valid == null) {
		player.getActionSender().sendSystemMessage("Please move to a more empty area");
		return;
	    }
	    int picid = -1;
	    if (amount < 10) {
		picid = 1090000;
	    } else if (amount < 100) {
		picid = 1090010;
	    } else if (amount < 1000) {
		picid = 1090020;
	    } else if (amount < 3000) {
		picid = 1091000;
	    } else if (amount < 10000) {
		picid = 1091010;
	    } else {
		picid = 1091020;
	    }

	    Item i = new Item(picid, 0, 0, 0, 0, 0);

	    int map = player.getCharacter().getMapid();
	    final GroundItem gi = new GroundItem(i.getUID(), i.getID(), i.getPlus(), i.getBless(), i.getEnchant(), i.getSoc1(), i.getSoc2(), (int) valid.getX(), (int) valid.getY(), map);
	    gi.setArrowAmount(amount);
	    player.getMap().addGroundItem(gi, valid, map);
	    player.getCharacter().setMoney(player.getCharacter().getMoney() - amount);
	    player.getActionSender().sendMoney();
	    player.getActionSender().sendSystemMessage("You have dropped " + amount + " silvers");
	    World.getWorld().getTimerService().add(new Timer(90000, null) {

		public void execute() {
		    if (gi != null) {
			World.getWorld().getMaps().get(gi.getMap()).getGroundItems().remove(gi);
			for (Player p : World.getWorld().getMaps().get(gi.getMap()).getPlayers().values()) {
			    if (Formula.inView(gi.getX(), gi.getY(), p.getCharacter().getX(), p.getCharacter().getY())) {
				p.getCharacter().getItemsInView().remove(gi);
				p.getActionSender().removeGroundItem(gi);

			    }
			}
		    }
		}
	    });


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

	case 27: // Ping (client pings every 10 seconds)
	    if(System.currentTimeMillis() - player.getLastPing() > 10000 + Constants.TIMED_OUT) {
		player.destroy(); // remove them upon timeout
		return;
	    } 
	    player.setLastPing(System.currentTimeMillis());
	    player.getActionSender().write(bb);
	    break;

	default:
	    System.out.println("Frame handler: ID " + id);
	}


    }
    static long lastTime = 0;
}
