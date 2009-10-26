package jonquer.model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jonquer.game.Constants;
import jonquer.net.PacketBuilder;
import jonquer.util.Crypto;
import jonquer.util.Formula;
import jonquer.util.Log;

import org.apache.mina.common.IoSession;

/**
 * All Player variables/properties will be held here. Each instance represents 1
 * player.
 * 
 * @author xEnt
 * 
 */
public class Player {

    /**
     * Create this Player object from a MINA session.
     * 
     * @param sess
     */
    public Player(IoSession sess, int id) {
	crypt = new Crypto();
	setCharacter(new Character(id));
	session = sess;
	currentIP = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress();
	currentLogin = System.currentTimeMillis();
	actionSender = new PacketBuilder(this);
	world.getPlayers().add(this);
    }

    /**
     * 
     * @return - the Class that sends packets to the client.
     */
    public PacketBuilder getPacketSender() {
	return this.actionSender;
    }

    /**
     * 
     * @return - the current IP used in a string.
     */
    public String getIP() {
	return this.currentIP;
    }

    /**
     * 
     * @return - the Queue of Incoming packets this Player has sent to server
     */
    public List<Packet> getIncomingPackets() {
	return this.incomingPackets;
    }

    /**
     * 
     * @return - the Queue of packets ready to be sent to the client.
     */
    public List<Packet> getOutgoingPackets() {
	return this.outgoingPackets;
    }

    /**
     * 
     * @return - the MINA session stream
     */
    public IoSession getSession() {
	return this.session;
    }

    /**
     * Called when a player disconnects, gets kicked or needs to leave the
     * server
     */
    public void destroy() {

	ObjectOutputStream oos;
	try {
	    oos = new ObjectOutputStream(new FileOutputStream(Constants.SAVED_GAME_DIRECTORY + getCharacter().getAccountName() + ".cfg"));
	    oos.writeObject(getCharacter());
	    oos.close();
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	crypt = null;
	Log.debug(this.getIP() + " has Left the server");
	getSession().close();
    }

    /**
     * Called when a player disconnects, gets kicked or needs to leave the
     * server
     */
    public void destroy(boolean nosave) {
	crypt = null;
	Log.debug(this.getIP() + " has Left the server");
	getSession().close();
    }

    public void updateSurroundings() {
	updateOthersToMe();
	updateMeToOthers();

    }

    public void updateOthersToMe() {
	for(Player p : World.getWorld().getPlayers()) {
	    if(p == this)
		continue;
	    if(Formula.inView(p.getCharacter(), getCharacter())) {
		// spawn them to us
		getActionSender().sendSpawnPacket(p.getCharacter());
	    }
	}
    }

    public void updateMeToOthers() {
	for(Player p : World.getWorld().getPlayers()) {
	    if(p == this)
		continue;
	    if(Formula.inView(p.getCharacter(), getCharacter())) {
		// spawn us to them
		p.getActionSender().sendSpawnPacket(getCharacter());
	    }
	}
    }

    /**
     * Retains all it's old Player object details.
     * 
     * @param p
     *            - the old Auth player object
     */
    public void retainObject(Player p, int key) {
	this.character = p.character;
	this.currentIP = p.currentIP;
	World.getWorld().getKeyPlayers().remove(key);
	p = null;
    }

    public Crypto getCrypt() {
	return crypt;
    }

    public void setCrypt(Crypto crypt) {
	this.crypt = crypt;
    }

    public String getCurrentIP() {
	return currentIP;
    }

    public void setCurrentIP(String currentIP) {
	this.currentIP = currentIP;
    }

    public long getCurrentLogin() {
	return currentLogin;
    }

    public void setCurrentLogin(long currentLogin) {
	this.currentLogin = currentLogin;
    }

    public PacketBuilder getActionSender() {
	return actionSender;
    }

    public void setActionSender(PacketBuilder actionSender) {
	this.actionSender = actionSender;
    }

    public void setIncomingPackets(List<Packet> incomingPackets) {
	this.incomingPackets = incomingPackets;
    }

    public void setOutgoingPackets(List<Packet> outgoingPackets) {
	this.outgoingPackets = outgoingPackets;
    }

    public void setSession(IoSession session) {
	this.session = session;
    }

    public void setCharacter(Character character) {
	this.character = character;
    }

    public Character getCharacter() {
	return character;
    }

    public static final World world = World.getWorld();
    public Crypto crypt;
    private Character character;
    private List<Packet> incomingPackets = Collections.synchronizedList(new ArrayList<Packet>());
    private List<Packet> outgoingPackets = Collections.synchronizedList(new ArrayList<Packet>());
    private String currentIP;
    private long currentLogin;
    private PacketBuilder actionSender;
    private IoSession session;

}
