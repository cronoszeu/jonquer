package jonquer.net;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import jonquer.game.Constants;
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

    /**
     * Sends the Auth info.
     */
    public void sendAuthInfo() {

	Constants.PEAK_PLAYER_COUNT++;
	int accID = 100000 + Constants.PEAK_PLAYER_COUNT;
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

    public void sendHeroInfo() {
	
	String name = player.getCharacter().getName();
	String spouse = player.getCharacter().getSpouse();
	ByteBuffer bb = ByteBuffer.allocate(70 + name.length() + spouse.length());
	bb.order(ByteOrder.LITTLE_ENDIAN);
	bb.putShort(0, (short) bb.limit());
	bb.putShort(2, (short) 1006); // packet id
	bb.putInt(4, player.getCharacter().getID()); // id
	bb.putInt(8, player.getCharacter().getModel()); // model
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

    private static byte[] createDataPacket(int timeStamp, int id, int arg0, short arg1, short arg2, short arg3) {
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

    public void write(ByteBuffer b) {
	byte[] buff = b.array();
	player.crypt.encrypt(buff);
	player.getSession().write(org.apache.mina.common.ByteBuffer.wrap(buff));
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
