package jonquer.net;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

import jonquer.game.Constants;
import jonquer.model.Item;
import jonquer.model.Player;
import jonquer.model.World;
import jonquer.util.Log;

import org.apache.mina.common.IoSession;

/**
 * Structures Packets and sends them from this class.
 * 
 * @author xEnt
 * 
 */
public class PacketBuilder {

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
    
    public void removeEntity(Player p, int prevX, int prevY) {
	ByteBuffer bb = ByteBuffer.allocate(24);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1010); // packet id
	bb.putInt(4, (int) System.currentTimeMillis());
	bb.putInt(8, p.getCharacter().getID());
	bb.putShort(12, (short)prevX);
	bb.putShort(14, (short)prevY);
	bb.putShort(16, (short) 0);
	bb.putShort(18, (short) 0);
	bb.putShort(20, (short) 0);
	bb.putShort(22, (short) 132);
	write(bb);
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
	for(Item i : player.getCharacter().getInventory().getItems())
	    sendItem(i);
    }

    public void sendItem(Item i) {
	ByteBuffer bb = ByteBuffer.allocate(36);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1008); // packet id
	bb.putInt(4, i.getUID()); // uid
	bb.putInt(8, i.getID()); // id
	bb.putShort(16, (short)1);

	if(i.getDef().isArrows()) {
	    bb.putShort(12, (short)i.getArrowAmount());
	    bb.put(14, (byte)1); // leave
	} else {
	    if(i.hasDurability()) { // sets durability (if possible)
		bb.putShort(12, (short)(i.getDurability() * 100)); 
		bb.putShort(14, (short)(i.getDef().getMaxDurability() * 100));
	    }
	}

	bb.put(18, (byte)0);
	bb.put(24, (byte)i.getSoc1());
	bb.put(25, (byte)i.getSoc2());
	bb.put(28, (byte)i.getPlus());
	bb.put(29, (byte)i.getBless());
	bb.put(30, (byte)i.getEnchant());
	write(bb);
    }
    
    public void sendJump(int x, int y, int prevX, int prevY, int id) {
	ByteBuffer bb = ByteBuffer.allocate(28);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1010); // packet id
	bb.putInt(4, (int)System.currentTimeMillis());
	bb.putInt(8, id);
	bb.putShort(12, (short)x);
	bb.putShort(14, (short)y);
	bb.putShort(16, (short)prevX);
	bb.putShort(18, (short)0);
	bb.putShort(20, (short)133);
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
	bb.putInt(8, Integer.parseInt("" + player.getCharacter().getAvatar() + "" + player.getCharacter().getModel())); // model
	bb.putShort(12, (short) player.getCharacter().getHairstyle()); // hairstyle
	bb.putInt(14, player.getCharacter().getMoney()); // money
	bb.putInt(18, player.getCharacter().getConquerPoints()); // CP
	bb.putShort(46, (short) player.getCharacter().getStrength()); // str
	bb.putShort(48, (short) player.getCharacter().getAgility()); // agi
	bb.putShort(50, (short) player.getCharacter().getVitality()); // vit
	bb.putShort(52, (short) player.getCharacter().getSpirit()); // spirit
	bb.putShort(54, (short) player.getCharacter().getStats()); // stats points?
	bb.putShort(56, (short) player.getCharacter().getHealthPoints()); // hp
	bb.putShort(58, (short) player.getCharacter().getManaPoints()); // mp
	bb.put(62, (byte) player.getCharacter().getLevel()); // lvl
	bb.put(63, (byte) player.getCharacter().getProfession()); // prof
	bb.put(64, (byte) 5);
	bb.put(65, (byte) 0);
	bb.put(66, (byte) 1);
	bb.put(67, (byte) 2);
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
	ByteBuffer bb = ByteBuffer.wrap(createDataPacket((int) System.currentTimeMillis(), player.getCharacter().getID(), player.getCharacter().getMap(), player.getCharacter().getX(), player.getCharacter().getY(), (short) 0));
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

    public void sendSpawnPacket(jonquer.model.Character hero) {
	ByteBuffer bb = ByteBuffer.allocate(85 + hero.getName().length());
	bb.order(ByteOrder.LITTLE_ENDIAN);

	int model = Integer.parseInt(hero.getAvatar() + "" + (hero.isDead() ? "1099" : hero.getModel()));
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 0x3f6);
	bb.putInt(4, hero.getID());
	bb.putInt(8, model);
	bb.putInt(12, hero.getStats());
	bb.putShort(48, hero.getHealthPoints());
	bb.put(50, (byte) hero.getLevel());
	//bb.putInt(28, ); head id
	//bb.putInt(32, ); armor id
	//bb.putInt(36, ); right hand id
	//bb.putInt(40, ); left hand id
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
	bb.put(14, (byte) direction);
	bb.putInt(16, flag);
	write(bb);
    }

    public void sendMonsterSpawn(int id, int mesh, int x, int y, String name,
	    int health, int level, int direction) {
	ByteBuffer bb = ByteBuffer.allocate(85 + name.length());
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1014);
	bb.putInt(4, id);
	bb.putInt(8, mesh);
	bb.putShort(48, (short) health);
	bb.putShort(50, (short) level);
	bb.putShort(52, (short) x);
	bb.putShort(54, (short) y);
	bb.put(58, (byte) direction);
	bb.put(59, (byte) 100);
	bb.put(80, (byte) 1);
	bb.put(81, (byte) name.length());
	for (int i = 0; i < name.length(); i++) {
	    bb.put(82 + i, (byte) name.charAt(i));
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

    public void write(ByteBuffer b) {
	byte[] buff = b.array();
	if (buff != null && b != null && buff.length > 4) {
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
