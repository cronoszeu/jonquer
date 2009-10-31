package jonquer.model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
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

    public void save() {
	ObjectOutputStream oos;
	try {
	    oos = new ObjectOutputStream(new FileOutputStream(Constants.SAVED_GAME_DIRECTORY + getCharacter().getAccountName() + ".cfg"));
	    oos.writeObject(getCharacter());
	    oos.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Called when a player disconnects, gets kicked or needs to leave the
     * server
     */
    public void destroy() {

	for(Player p : World.getWorld().getPlayers()) {
	    if(p.getPlayersInView().contains(this)) {
		p.getPlayersInView().remove(this);
		p.getActionSender().removeEntity(this);
	    }
	}
	getPlayersInView().clear();
	try {
	    world.getPlayers().remove(this);
	} catch(ConcurrentModificationException cme) { }
	save();
	Log.debug(this.getIP() + " has Left the server");
	getSession().close();
	crypt = null;
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


    public void updateOthersToMe() {
	for(Player p : getPlayersInView()) {
	    getActionSender().sendSpawnPacket(p.getCharacter());
	}
    }

    public void updatePosition(Player p) {
	updatePosition(p, true, null, -1 , -1);
    }



    public void updatePosition(Player p, boolean spawn, ByteBuffer data, int prevX, int prevY) {

	/*if(!spawn) {
	    if(Formula.inFarView(p.getCharacter(), getCharacter()) && !Formula.inView(p.getCharacter(), getCharacter())) {
		if(!Formula.inFarView(prevX, prevY, getCharacter().getX(), getCharacter().getY())) { // jumping into far view
		    getActionSender().sendSpawnPacket(p.getCharacter());
		    p.getActionSender().sendSpawnPacket(getCharacter());
		}
		if(getPlayersInView().contains(p)) {
		    getPlayersInView().remove(p);
		    p.getPlayersInView().remove(this);
		}
	    }
	    else
	    // leaving far view
	    if(Formula.inFarView(prevX, prevY, getCharacter().getX(), getCharacter().getY()) && !Formula.inFarView(p.getCharacter().getX(), p.getCharacter().getY(), getCharacter().getX(), getCharacter().getY())) {
		if(getPlayersInView().contains(p) && Formula.inView(prevX, prevY, getCharacter().getX(), getCharacter().getY())) {
		    getPlayersInView().remove(p);
		    p.getPlayersInView().remove(this);
		}
		getActionSender().removeEntity(p);
		p.getActionSender().removeEntity(this);
	    }
	    else
	    //from close view to far view
	    if(!Formula.inView(getCharacter(), p.getCharacter()) && Formula.inFarView(getCharacter(), p.getCharacter()) && Formula.inView(prevX, prevY, getCharacter().getX(), getCharacter().getY())) {
		if(!getPlayersInView().contains(p))
		    getPlayersInView().remove(p);
		if(!p.getPlayersInView().contains(this))
		    p.getPlayersInView().remove(this);
		
		getActionSender().write(data);
		p.getActionSender().write(data);
	    }
	    else
	    // jumping from far view to close view
	    if(Formula.inFarView(prevX, prevY, getCharacter().getX(), getCharacter().getY()) && !Formula.inView(prevX, prevY, getCharacter().getX(), getCharacter().getY()) && Formula.inView( getCharacter(), p.getCharacter())) {
		if(!getPlayersInView().contains(p))
		    getPlayersInView().add(p);
		if(!p.getPlayersInView().contains(this))
		    p.getPlayersInView().add(this);
		getActionSender().write(data);
		p.getActionSender().write(data);
	    }
	    else
		if(Formula.inView(p.getCharacter(), getCharacter()) || Formula.inFarView(p.getCharacter(), getCharacter())) {
		    System.out.println("yep in view or far view");
		    //if(getPlayersInView().contains(p))
			//getActionSender().write(data);
		   // if(p.getPlayersInView().contains(this))
			//p.getActionSender().write(data);
		}
	    //if in view
	} else {*/
	    //////////--------------------------------------------------------------------------- new shit above me


	    if(Formula.inView(prevX, prevY, getCharacter().getX(), getCharacter().getY())) {
		if(!Formula.inView(p.getCharacter(), getCharacter())) { // leaving/left
		    getPlayersInView().remove(p);
		    p.getPlayersInView().remove(this);
		    if(!spawn) {    
			getActionSender().write(data);
			p.updateMeToOthers();
		    }
		    else {
			p.updateMeToOthers();
			updateMeToOthers();  
			p.getActionSender().removeEntity(this);
			getActionSender().removeEntity(p);
		    }
		} else {
		    if(!spawn) {
			p.updateOthersToMe();
			updateMeToOthers();  // ----
			getActionSender().write(data);
		    }
		    else {
			p.updateMeToOthers();
			updateMeToOthers();   
		    }
		}
	    } else { // not in view
		if(Formula.inView(p.getCharacter(), getCharacter())) { // going in
		    if(!getPlayersInView().contains(p))
			getPlayersInView().add(p);
		    if(!p.getPlayersInView().contains(this))
			p.getPlayersInView().add(this);
		    if(!spawn) {
			updateOthersToMe(); // ---
			// updateMeToOthers();
			p.updateOthersToMe();
			getActionSender().write(data);
		    }
		    else {
			p.updateMeToOthers();
			updateMeToOthers(); 
		    }
		} else {
		    if(!spawn)
			getActionSender().write(data);
		    else {
			p.updateMeToOthers();
			updateMeToOthers();   
		    }
		}
	    }
	
	/*if(getPlayersInView().contains(p)) {
	    if(Formula.inView(getCharacter(), p.getCharacter())) {

	    }
	}

	if(getPlayersInView().contains(p)) {
	    if(Formula.inView(getCharacter(), p.getCharacter())) {
		getPlayersInView().add(p);
		p.getPlayersInView().add(this);
		if(spawn) {
		    p.updateOthersToMe();
		    updateOthersToMe();
		}
	    } 
	} else {
	    if(!Formula.inView(p.getCharacter(), getCharacter())) {
		getPlayersInView().remove(p);
		p.getPlayersInView().remove(this);
		if(spawn) {
		    p.updateOthersToMe();
		    updateOthersToMe();
		} else {
		    getActionSender().write(data);
		}
	    } else {
	    }
	}*/
    }

    public void updateMeToOthers() {
	for(Player p : getPlayersInView()) {
	    p.getActionSender().sendSpawnPacket(getCharacter());
	}
    }

    public ArrayList<Player> getPlayersInView() {
	return this.playersInView;
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

    public void setLocked(boolean locked) {
	this.locked = locked;
    }

    public boolean isLocked() {
	return locked;
    }

    /**
     * List of players in your view area.
     */
    private ArrayList<Player> playersInView = new ArrayList<Player>();
    public static final World world = World.getWorld();
    private boolean locked = false;
    public Crypto crypt;
    private Character character;
    private List<Packet> incomingPackets = Collections.synchronizedList(new ArrayList<Packet>());
    private List<Packet> outgoingPackets = Collections.synchronizedList(new ArrayList<Packet>());
    private String currentIP;
    private long currentLogin;
    private PacketBuilder actionSender;
    private IoSession session;

}
