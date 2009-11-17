package jonquer.net;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import jonquer.debug.Log;
import jonquer.misc.Constants;
import jonquer.misc.Formula;
import jonquer.model.GroundItem;
import jonquer.model.Item;
import jonquer.model.Monster;
import jonquer.model.Player;
import jonquer.model.World;

import org.apache.mina.common.IoSession;

/**
 * Structures Packets and sends them from this class.
 * 
 * @author xEnt
 * 
 */
public class PacketBuilder {
    
    
    public void giveSkill(int skillID, int lv, int exp) {
	ByteBuffer bb = ByteBuffer.allocate(12);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1103); // packet id
	bb.putInt(4, exp);
	bb.putShort(8, (short)skillID);
	bb.putShort(10, (short)lv);
	write(bb);
    }

    public void fadeMonster(Monster m) {
	ByteBuffer bb = ByteBuffer.allocate(28);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1017); // packet id
	bb.putInt(4, m.getUID());
	bb.put(8, (byte)1);
	bb.put(12, (byte)26);
	bb.putShort(16, (short)2080);
	write(bb);
    }//1010

    public void removeMonster(Monster m) {
	ByteBuffer bb = ByteBuffer.allocate(24);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1010); // packet id
	bb.putInt(4, (int) System.currentTimeMillis());
	bb.putInt(8, m.getUID());
	bb.putShort(12, (short) player.getCharacter().getMapid());
	bb.putShort(14, (short) m.getX());
	bb.putShort(16, (short) m.getY());
	bb.putShort(18, (short) 0);
	bb.putShort(20, (short) 0);
	bb.putShort(22, (short) 132);
	write(bb);

    }

    public void respawnMonster(Monster m) {
	String name = "MBStandard";
	ByteBuffer bb = ByteBuffer.allocate(13 + name.length());
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1015); // packet id
	bb.putInt(4, m.getUID());
	bb.put(8, (byte)10);
	bb.put(9, (byte)1);
	bb.put(10, (byte)name.length());

	for (int i = 0; i < name.length(); i++) {
	    bb.put(11 + i, (byte) name.charAt(i));
	}
	write(bb);
    }



    public void attack(Player attacker, int targetUID, int targetX, int targetY, int attackType, int damage) {

	ByteBuffer bb = ByteBuffer.allocate(28);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1022); // packet id
	bb.putInt(8, attacker.getCharacter().getID());
	bb.putInt(12, targetUID);
	bb.putShort(16, (short)targetX);
	bb.putShort(18, (short)targetY);
	bb.putInt(20, attackType);
	bb.putInt(24, damage);
	write(bb);
    }

    public void sendFightMode() {
	ByteBuffer bb = ByteBuffer.allocate(24);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1010); // packet id
	bb.putInt(4, (int) System.currentTimeMillis());
	bb.putInt(8, player.getCharacter().getID());
	bb.putShort(12, (short) player.getCharacter().getFightmode());
	bb.putShort(14, (short) 0);
	bb.putShort(16, (short) 0);
	bb.putShort(18, (short) 0);
	bb.putShort(20, (short) 0);
	bb.putShort(22, (short) 96);
	write(bb);
    }

    public void spawnGroundItem(GroundItem i) {
	ByteBuffer bb = ByteBuffer.allocate(20);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1101); // packet id
	bb.putInt(4, i.getUID());
	bb.putInt(8, i.getID());

	bb.putShort(12, (short)i.getX());
	bb.putShort(14, (short)i.getY());
	bb.put(16, (byte)1);
	bb.putShort(18, (short)i.getMap());
	write(bb);
    }

    public void removeItemEffect(GroundItem i) {
	ByteBuffer bb = ByteBuffer.allocate(20);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1101); // packet id
	bb.putInt(4, i.getUID());
	bb.putInt(8, i.getID());

	bb.putShort(12, (short)i.getX());
	bb.putShort(14, (short)i.getY());
	bb.put(16, (byte)3);
	bb.putShort(18, (short)i.getMap());
	write(bb);
    }

    public void removeGroundItem(GroundItem i) {
	ByteBuffer bb = ByteBuffer.allocate(20);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1101); // packet id
	bb.put(8, (byte)0x4d);
	bb.put(9, (byte)0xa2);
	bb.putInt(4, i.getUID());
	bb.putInt(8, i.getID());
	bb.putShort(10, (short)Formula.rand(1, 9));
	bb.putShort(12, (short)Formula.rand(99, 153)); // wtf?
	bb.putShort(14, (short)Formula.rand(208, 273));
	bb.put(16, (byte)2);
	write(bb);
    }

    /**
     * Sends the Auth info.
     */
    public void sendAuthInfo() {

	Constants.PEAK_PLAYER_COUNT++;
	int accID = 1000001 + Constants.PEAK_PLAYER_COUNT;
	int port = 5816;
	player.getCharacter().setID(accID);
	String host = Constants.GAME_HOST;
	ByteBuffer bb = ByteBuffer.allocate(32);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1055);
	bb.putInt(4, 255);
	bb.putInt(8, accID);
	for (int i = 0; i < host.length(); i++) {
	    bb.put(12 + i, (byte) host.charAt(i));
	}
	bb.putShort(28, (short) port);
	write(bb);
	World.getWorld().getKeyPlayers().put(accID, player);
    }

    public void removeItem(Item i) {
	ByteBuffer bb = ByteBuffer.allocate(20);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1009); // packet id
	bb.putInt(4, i.getUID());
	bb.putInt(8, 0);
	bb.putInt(12, 3);
	write(bb);

    }

    public void removeEntity(Player p, int prevX, int prevY) {
	ByteBuffer bb = ByteBuffer.allocate(24);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1010); // packet id
	bb.putInt(4, (int) System.currentTimeMillis());
	bb.putInt(8, p.getCharacter().getID());
	bb.putShort(12, (short) prevX);
	bb.putShort(14, (short) prevY);
	bb.putShort(16, (short) 0);
	bb.putShort(18, (short) 0);
	bb.putShort(20, (short) 0);
	bb.putShort(22, (short) 132);
	write(bb);
    }

    public void sendSystemMessage(String msg) {
	sendMessage(0xffff, Formula.TOP_LEFT_MESSAGE_TYPE, "SYSTEM", "ALL", msg);
    }

    public void removeEntity(Player p) {
	ByteBuffer bb = ByteBuffer.allocate(24);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1010); // packet id
	bb.putInt(4, (int) System.currentTimeMillis());
	bb.putInt(8, p.getCharacter().getID());
	bb.putInt(12, 0);
	bb.putShort(16, (short) 0);
	bb.putShort(18, (short) 0);
	bb.putShort(22, (short) 132);
	write(bb);
    }

    public void sendInventory() {
	for (Item i : player.getCharacter().getInventory().getItems()) {
	    sendItem(i);
	}
    }

    public void sendStamina() {
	sendUpdatePacket(9, player.getCharacter().getStamina());
    }


    public void sendProfs() {
	int type = -1;
	type = Formula.PROF_BLADE;
	sendProf(type, player.getCharacter().getProficiency_level().get(type), player.getCharacter().getProficiency().get(type));
	type = Formula.PROF_SWORD;
	if(player.getCharacter().getProficiency_level().get(type) > 0)
	    sendProf(type, player.getCharacter().getProficiency_level().get(type), player.getCharacter().getProficiency().get(type));
	type = Formula.PROF_BACKSWORD;
	sendProf(type, player.getCharacter().getProficiency_level().get(type), player.getCharacter().getProficiency().get(type));
	type = Formula.PROF_HOOK;
	sendProf(type, player.getCharacter().getProficiency_level().get(type), player.getCharacter().getProficiency().get(type));
	type = Formula.PROF_WHIP;
	sendProf(type, player.getCharacter().getProficiency_level().get(type), player.getCharacter().getProficiency().get(type));
	type = Formula.PROF_AXE;
	sendProf(type, player.getCharacter().getProficiency_level().get(type), player.getCharacter().getProficiency().get(type));
	type = Formula.PROF_HAMMER;
	sendProf(type, player.getCharacter().getProficiency_level().get(type), player.getCharacter().getProficiency().get(type));
	type = Formula.PROF_CLUB;
	sendProf(type, player.getCharacter().getProficiency_level().get(type), player.getCharacter().getProficiency().get(type));
	type = Formula.PROF_SCEPTER;
	sendProf(type, player.getCharacter().getProficiency_level().get(type), player.getCharacter().getProficiency().get(type));
	type = Formula.PROF_DAGGER;
	sendProf(type, player.getCharacter().getProficiency_level().get(type), player.getCharacter().getProficiency().get(type));
	type = Formula.PROF_BOW;
	sendProf(type, player.getCharacter().getProficiency_level().get(type), player.getCharacter().getProficiency().get(type));
	type = Formula.PROF_GLAIVE;
	sendProf(type, player.getCharacter().getProficiency_level().get(type), player.getCharacter().getProficiency().get(type));
	type = Formula.PROF_POLEAXE;
	sendProf(type, player.getCharacter().getProficiency_level().get(type), player.getCharacter().getProficiency().get(type));
	type = Formula.PROF_LONGHAMMER;
	sendProf(type, player.getCharacter().getProficiency_level().get(type), player.getCharacter().getProficiency().get(type));
	type = Formula.PROF_SPEAR;
	sendProf(type, player.getCharacter().getProficiency_level().get(type), player.getCharacter().getProficiency().get(type));
	type = Formula.PROF_WAND;
	sendProf(type, player.getCharacter().getProficiency_level().get(type), player.getCharacter().getProficiency().get(type));
	type = Formula.PROF_HALBERT;
	sendProf(type, player.getCharacter().getProficiency_level().get(type), player.getCharacter().getProficiency().get(type));

    }

    public void sendProf(int type, int lv, int exp) {
	ByteBuffer bb = ByteBuffer.allocate(16);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 0x401); // packet id
	bb.putInt(4, type);
	bb.putInt(8, lv);
	bb.putInt(12, exp);
	write(bb);
    }

    public void sendItem(Item i) {
	sendItem(i, (byte)0);
    }

    public void sendItem(Item i, byte pos) {
	ByteBuffer bb = ByteBuffer.allocate(36);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1008); // packet id
	bb.putInt(4, i.getUID()); // uid
	bb.putInt(8, i.getID()); // id
	bb.putShort(16, (short) 1);

	if (i.getDef().isArrows()) {
	    bb.putShort(12, (short) i.getArrowAmount());
	    bb.put(14, (byte) 1); // leave
	} else {
	    if (i.hasDurability()) { // sets durability (if possible)
		System.out.println(i.getDurability());
		System.out.println(i.getDef().getMaxDurability());
		bb.putShort(12, (short) (i.getDurability() * 100));
		bb.putShort(14, (short) (i.getDef().getMaxDurability() * 100));
	    }
	}

	bb.put(18, (byte) pos);
	bb.put(24, (byte) i.getSoc1());
	bb.put(25, (byte) i.getSoc2());
	bb.put(28, (byte) i.getPlus());
	bb.put(29, (byte) i.getBless());
	bb.put(30, (byte) i.getEnchant());
	write(bb);
    }

    public void sendJump(int x, int y, int prevX, int prevY, int id) {
	ByteBuffer bb = ByteBuffer.allocate(28);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1010); // packet id
	bb.putInt(4, (int) System.currentTimeMillis());
	bb.putInt(8, id);
	bb.putShort(12, (short) x);
	bb.putShort(14, (short) y);
	bb.putShort(16, (short) prevX);
	bb.putShort(18, (short) 0);
	bb.putShort(20, (short) 133);
	bb.putInt(22, 0);
	write(bb);
    }

    public void sendHeroInfo() {

	String name = player.getCharacter().getName();
	String spouse = player.getCharacter().getSpouse();
	ByteBuffer bb = ByteBuffer.allocate(70 + name.length() + spouse.length());
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1006); // packet id
	bb.putInt(4, player.getCharacter().getID()); // id
	bb.putInt(8, Integer.parseInt("" + player.getCharacter().getFace() + "" + player.getCharacter().getLook())); // model
	bb.putShort(12, (short) player.getCharacter().getHairstyle()); // hairstyle
	bb.putInt(14, player.getCharacter().getMoney()); // money
	bb.putInt(18, player.getCharacter().getConquerPoints()); // CP
	bb.putShort(46, (short) player.getCharacter().getStrength()); // str
	bb.putShort(48, (short) player.getCharacter().getAgility()); // agi
	bb.putShort(50, (short) player.getCharacter().getVitality()); // vit
	bb.putShort(52, (short) player.getCharacter().getSpirit()); // spirit
	bb.putShort(54, (short) player.getCharacter().getStats()); // stats points?
	bb.putShort(56, (short) player.getCharacter().getLife()); // hp
	bb.putShort(58, (short) player.getCharacter().getMana()); // mp
	bb.put(62, (byte) player.getCharacter().getLevel()); // lvl
	bb.put(63, (byte) player.getCharacter().getProfession()); // prof
	bb.put(64, (byte) 5); // 5
	bb.put(65, (byte) 0);
	bb.put(66, (byte) 1);
	bb.put(67, (byte) 0);
	bb.put(68, (byte) name.length());
	for (int i = 0; i < name.length(); i++) {
	    bb.put(69 + i, (byte) name.charAt(i));
	}
	bb.put(69 + name.length(), (byte) spouse.length());
	for (int i = 0; i < spouse.length(); i++) {
	    bb.put(70 + name.length() + i, (byte) spouse.charAt(i));
	}
	write(bb);
    }

    public void sendMessage(int color, short messageType, String from, String to, String message) {
	ByteBuffer bb = ByteBuffer.allocate(32 + from.length() + to.length() + message.length());
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1004);
	bb.putInt(4, color);
	bb.putShort(8, messageType);
	bb.put(24, (byte) 4);
	bb.put(25, (byte) from.length());
	for (int i = 0; i < from.length(); i++) {
	    bb.put(26 + i, (byte) from.charAt(i));
	}
	bb.put(26 + from.length(), (byte) to.length());
	for (int i = 0; i < to.length(); i++) {
	    bb.put(27 + from.length() + i, (byte) to.charAt(i));
	}
	bb.put(28 + from.length() + to.length(), (byte) message.length());
	for (int i = 0; i < message.length(); i++) {
	    bb.put(29 + from.length() + to.length() + i, (byte) message.charAt(i));
	}
	write(bb);
    }

    public void sendLocation() {
	ByteBuffer bb = ByteBuffer.wrap(createDataPacket((int) System.currentTimeMillis(), player.getCharacter().getID(), player.getCharacter().getMapid(), player.getCharacter().getX(), player.getCharacter().getY(), (short) player.getCharacter().getDirection()));
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(22, (short) 74).array();
	write(bb);
    }

    private byte[] createDataPacket(int timeStamp, int id, int arg0, short arg1, short arg2, short arg3) {
	ByteBuffer bb = ByteBuffer.allocate(24);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1010);
	bb.putInt(4, timeStamp);
	bb.putInt(8, id);
	bb.putInt(12, arg0);
	bb.putShort(16, arg1);
	bb.putShort(18, arg2);
	bb.putShort(20, arg3);
	return bb.array();
    }

    public void sendViewedEquips(Player toview) {
	ByteBuffer bb = ByteBuffer.allocate(11 + toview.getCharacter().getSpouse().length());
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 0x3f7);
	bb.putInt(4, toview.getCharacter().getID());
	bb.put(8, (byte)0x10);
	bb.put(9, (byte)0x01);
	bb.put(10, (byte)toview.getCharacter().getSpouse().length());
	for (int i = 0; i < toview.getCharacter().getSpouse().length(); i++) {
	    bb.put(11 + i, (byte) toview.getCharacter().getSpouse().charAt(i));
	}
	write(bb);
	sendViewEquipItem(toview.getCharacter().getEquipment().getHead(), Formula.ARMET_EQUIP_SLOT, toview);
	sendViewEquipItem(toview.getCharacter().getEquipment().getArmor(), Formula.ARMOR_EQUIP_SLOT, toview);
	sendViewEquipItem(toview.getCharacter().getEquipment().getNeck(), Formula.NECKLACE_BAG_EQUIP_SLOT, toview);
	sendViewEquipItem(toview.getCharacter().getEquipment().getRing(), Formula.RING_EQUIP_SLOT, toview);
	sendViewEquipItem(toview.getCharacter().getEquipment().getBoots(), Formula.BOOT_EQUIP_SLOT, toview);
	sendViewEquipItem(toview.getCharacter().getEquipment().getLeft_hand(), Formula.LEFT_WEAPON_EQUIP_SLOT, toview);
	sendViewEquipItem(toview.getCharacter().getEquipment().getRight_hand(), Formula.RIGHT_WEAPON_EQUIP_SLOT, toview);
    }

    private void sendViewEquipItem(Item i, int slot, Player toview) {
	ByteBuffer bb = ByteBuffer.allocate(36);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1008);
	bb.putInt(4, toview.getCharacter().getID());

	if(i != null) {
	    bb.putInt(8, i.getID());
	    if (i.getDef().isArrows()) {
		bb.putShort(12, (short) i.getArrowAmount());
		bb.put(14, (byte) 1); // leave
	    } else {
		if (i.hasDurability()) { // sets durability (if possible)
		    bb.putShort(12, (short) (i.getDurability() * 100));
		    bb.putShort(14, (short) (i.getDef().getMaxDurability() * 100));
		}
	    }


	    bb.put(24, (byte) i.getSoc1());
	    bb.put(25, (byte) i.getSoc2());
	    bb.put(28, (byte) i.getPlus());
	    bb.put(29, (byte) i.getBless());
	    bb.put(30, (byte) i.getEnchant());
	}
	bb.put(18, (byte) slot);
	bb.put(16, (byte)4);
	write(bb);

    }
    public void sendSpawnPacket(jonquer.model.Character hero) {
	ByteBuffer bb = ByteBuffer.allocate(85 + hero.getName().length());
	bb.order(ByteOrder.LITTLE_ENDIAN);

	int model = Integer.parseInt(hero.getFace() + "" + (hero.isDead() ? "1099" : hero.getLook()));
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 0x3f6);
	bb.putInt(4, hero.getID());
	bb.putInt(8, model);
	bb.putInt(12, hero.getStats());
	bb.putShort(48, hero.getLife());
	bb.put(50, (byte) hero.getLevel());
	if(hero.getEquipment().getHead() != null)
	    bb.putInt(28, hero.getEquipment().getHead().getID()); //head id
	if(hero.getEquipment().getArmor() != null)
	    bb.putInt(32, hero.getEquipment().getArmor().getID()); 
	if(hero.getEquipment().getRight_hand() != null)
	    bb.putInt(36, hero.getEquipment().getRight_hand().getID());
	if(hero.getEquipment().getLeft_hand() != null)
	    bb.putInt(40, hero.getEquipment().getLeft_hand().getID());
	bb.putShort(52, hero.getX());
	bb.putShort(54, hero.getY());
	bb.putShort(56, (short) hero.getHairstyle()); // hair
	bb.put(58, (byte) hero.getDirection());
	bb.put(59, (byte) hero.getAction()); // action
	bb.put(60, hero.getReborn());
	bb.put(62, (byte) hero.getLevel());
	bb.put(80, (byte) 1);
	bb.put(81, (byte) hero.getName().length());
	for (int i = 0; i < hero.getName().length(); i++) {
	    bb.put(82 + i, (byte) hero.getName().charAt(i));
	}
	write(bb);
    }

    public void sendUpdatePacket(int updateType, int value) {
	ByteBuffer bb = ByteBuffer.allocate(20);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) 20);
	bb.putShort(2, (short) 1017);
	bb.putInt(4, player.getCharacter().getID());
	bb.put(8, (byte) 1);
	bb.putInt(12, updateType);
	bb.putInt(16, value);
	write(bb);
    }

    public void sendNpcSpawn(int id, int x, int y,
	    int type, int direction, int flag) {
	ByteBuffer bb = ByteBuffer.allocate(0x14);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 0x7ee);
	bb.putInt(4, id);
	bb.putShort(8, (short) x);
	bb.putShort(10, (short) y);
	bb.putShort(12, (short) type);
	bb.putShort(14, (short) flag);
	bb.put(16, (byte) direction);
	write(bb);
    }

    public void sendMonsterSpawn(Monster monster) {
	if (monster.getDef() == null) {
	    System.out.println("why are some monster spawns getting nulled??");
	    return;
	}

	ByteBuffer bb = ByteBuffer.allocate(85 + monster.getDef().getName().length());
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1014);
	bb.putInt(4, monster.getUID());
	bb.putInt(8, monster.getDef().getLookface());
	;
	bb.putShort(48, (short) monster.getDef().getLife());
	bb.putShort(50, (short) monster.getDef().getLevel());
	bb.putShort(52, (short) monster.getX());
	bb.putShort(54, (short) monster.getY());
	bb.put(58, (byte) 0);
	bb.put(59, (byte) 100);
	bb.put(80, (byte) 1);
	bb.put(81, (byte) monster.getDef().getName().length());
	for (int i = 0; i < monster.getDef().getName().length(); i++) {
	    bb.put(82 + i, (byte) monster.getDef().getName().charAt(i));
	}
	write(bb);
    }

    public void sendNpcDialogPacket(String dialog) {
	ByteBuffer bb = ByteBuffer.allocate(16 + dialog.length());
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 2032);
	bb.put(10, (byte) 255);
	bb.put(11, (byte) 1);
	bb.put(12, (byte) 1);
	bb.put(13, (byte) dialog.length());
	for (int i = 0; i < dialog.length(); i++) {
	    bb.put(14 + i, (byte) dialog.charAt(i));
	}
	write(bb);
    }

    public void sendNpcDialogOptionPacket(String option, int value) {
	ByteBuffer bb = ByteBuffer.allocate(16 + option.length());
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 2032);
	bb.put(10, (byte) value);
	bb.put(11, (byte) 2);
	bb.put(12, (byte) 1);
	bb.put(13, (byte) option.length());
	for (int i = 0; i < option.length(); i++) {
	    bb.put(14 + i, (byte) option.charAt(i));
	}
	write(bb);
    }

    public void sendNpcDialogFacePacket(int face) {
	ByteBuffer bb = ByteBuffer.allocate(16);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 2032);
	bb.put(4, (byte) 10);
	bb.put(6, (byte) 10);
	bb.putShort(8, (short) face);
	bb.put(10, (byte) 255);
	bb.put(11, (byte) 4);
	write(bb);
    }

    public void sendNpcDialogCompletePacket() {
	ByteBuffer bb = ByteBuffer.allocate(16);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 2032);
	bb.put(10, (byte) 255);
	bb.put(11, (byte) 100);
	write(bb);
    }

    public void sendEquippedItems() {
	if(player.getCharacter().getEquipment().getHead() != null) {
	    sendEquippedItem(player.getCharacter().getEquipment().getHead(), Formula.ARMET_EQUIP_SLOT);
	}
	if(player.getCharacter().getEquipment().getRing() != null) {
	    sendEquippedItem(player.getCharacter().getEquipment().getRing(), Formula.RIGHT_WEAPON_EQUIP_SLOT);
	}
	if(player.getCharacter().getEquipment().getBoots() != null) {
	    sendEquippedItem(player.getCharacter().getEquipment().getBoots(), Formula.BOOT_EQUIP_SLOT);
	}
	if(player.getCharacter().getEquipment().getArmor() != null) {
	    sendEquippedItem(player.getCharacter().getEquipment().getArmor(), Formula.ARMOR_EQUIP_SLOT);
	}
	if(player.getCharacter().getEquipment().getNeck() != null) {
	    sendEquippedItem(player.getCharacter().getEquipment().getNeck(), Formula.NECKLACE_BAG_EQUIP_SLOT);
	}
	if(player.getCharacter().getEquipment().getLeft_hand() != null) {
	    sendEquippedItem(player.getCharacter().getEquipment().getLeft_hand(), Formula.LEFT_WEAPON_EQUIP_SLOT);
	}
	if(player.getCharacter().getEquipment().getRight_hand() != null) {
	    sendEquippedItem(player.getCharacter().getEquipment().getRight_hand(), Formula.RIGHT_WEAPON_EQUIP_SLOT);
	}
    }

    public void sendUnequipItem(Item item, byte slot) {
	ByteBuffer bb = ByteBuffer.allocate(20);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1009);
	bb.putInt(4, item.getUID());
	bb.putInt(8, slot);
	bb.putInt(12, 6);
	write(bb);
    }

    public void pickupDroppedItemAction(GroundItem i) {
	ByteBuffer bb = ByteBuffer.allocate(20);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1101);
	bb.putInt(4, player.getCharacter().getID());
	bb.putInt(8, i.getUID());
	bb.putShort(12, (short)i.getX());
	bb.putShort(14, (short)i.getY());
	bb.putShort(16, (short)4);
	bb.putInt(18, 4);
	bb.putInt(18, 0);
	write(bb); 
    }

    public void sendEquippedItem(Item item, byte slot) {
	ByteBuffer bb = ByteBuffer.allocate(36);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1008);
	bb.putInt(4, item.getUID());
	bb.putInt(8, item.getID());
	bb.putShort(16, (short) 1);
	if (item.getDef().isArrows()) {
	    bb.putShort(12, (short) item.getArrowAmount());
	    bb.put(14, (byte) 1);
	} else {
	    if (item.hasDurability()) {
		bb.putShort(12, (short) (item.getDurability() * 100));
		bb.putShort(14, (short) (item.getDef().getMaxDurability() * 100));
	    }
	}
	bb.put(18, slot);
	bb.put(24, (byte) item.getSoc1());
	bb.put(25, (byte) item.getSoc2());
	bb.put(28, (byte) item.getPlus());
	bb.put(29, (byte) item.getBless());
	bb.put(30, (byte) item.getEnchant());
	write(bb);
    }

    public void sendMoney() {
	ByteBuffer bb = ByteBuffer.allocate(28);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1017);
	bb.putInt(4, player.getCharacter().getID());
	bb.put(8, (byte)1);
	bb.putInt(12, 4);
	bb.putInt(16, player.getCharacter().getMoney());
	write(bb);    
    }

    public void sendCompleteMapChange() {
	ByteBuffer bb = ByteBuffer.wrap(createDataPacket((int) System.currentTimeMillis(), player.getCharacter().getID(), 0xffffffff, player.getCharacter().getX(), player.getCharacter().getY(), (short) 0));
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(22, (short) 0x68).array();
	write(bb);
    }

    public void sendMapInfo() {
	ByteBuffer bb = ByteBuffer.allocate(16);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1110);
	bb.putInt(4, player.getCharacter().getMapid());
	bb.putInt(8, player.getCharacter().getMapid());
	bb.putInt(8, 0000); //map type
	write(bb);
    }

    public void sendLootItem(int playerid, short cellx, short celly) {
	ByteBuffer bb = ByteBuffer.allocate(0x14);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1101);
	bb.putInt(4, playerid);
	bb.put(8, (byte) 0x00);
	bb.put(9, (byte) 0x00);
	bb.put(10, (byte) 0x00);
	bb.put(11, (byte) 0x00);
	bb.putShort(12, cellx);
	bb.putShort(14, celly);
	bb.put(16, (byte) 0x03);
	bb.put(17, (byte) 0x00);
	bb.put(18, (byte) 0x00);
	bb.put(19, (byte) 0x00);
	write(bb);
    }

    public void sendWeather(int weatherType, int intensity, int direction, int appearance) {
        ByteBuffer msg = ByteBuffer.allocate(0x14);
	msg.order(ByteOrder.LITTLE_ENDIAN);
        msg.putShort((short) 0x14);
        msg.putShort(CONetMsgType.WEATHER);
        msg.putInt(weatherType);
        msg.putInt(intensity);
        msg.putInt(direction);
        msg.putInt(appearance);
        write(msg);
    }

    public void sendStringInfo(int stringID, byte stringType, String string) {
        ByteBuffer msg = ByteBuffer.allocate(0xa + string.length());
	msg.order(ByteOrder.LITTLE_ENDIAN);
        msg.putShort((short) (0xa + string.length()));
        msg.putShort(CONetMsgType.STRING_INFO);
        msg.putInt(stringID);
        msg.put(stringType);
        msg.put((byte) string.length());
        msg.put(string.getBytes());
        write(msg);
    }

    public void write(ByteBuffer b) {
	byte[] buff = b.array();
	if (buff != null && b != null && buff.length > 4 || player.crypt != null) {
	    player.crypt.encrypt(buff);
	    player.getSession().write(org.apache.mina.common.ByteBuffer.wrap(buff));
	} else {
	    Log.error("Not enuff data to send!111!!!");
	}
    }

    /**
     * Each Player will have an instance of this Class.
     * 
     * @param p
     *            - the given Player
     */
    public PacketBuilder(Player p) {
	player = p;
	session = player.getSession();
    }
    /**
     * the Player
     */
    private Player player;
    /**
     * the MINA Session stream assigned to the Player
     */
    private IoSession session;
}
