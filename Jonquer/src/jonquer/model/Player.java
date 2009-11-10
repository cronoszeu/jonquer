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

import jonquer.debug.Log;
import jonquer.misc.Constants;
import jonquer.misc.Crypto;
import jonquer.misc.Formula;
import jonquer.misc.Script;
import jonquer.misc.StaticData;
import jonquer.net.PacketBuilder;
import jonquer.services.IoService;

import org.apache.mina.common.IoSession;

import bsh.Interpreter;

/**
 * All Player variables/properties will be held here. Each instance represents 1
 * player.
 * 
 * @author xEnt
 * 
 */
public class Player extends Entity {

    /**
     * Create this Player object from a MINA session.
     * 
     * @param sess
     */
    public Player(IoSession sess, int id) {
	id = Formula.rand(0, 1000001);
	crypt = new Crypto();
	setCharacter(new Character(id));
	getCharacter().ourPlayer = this;
	session = sess;
	currentIP = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress();
	currentLogin = System.currentTimeMillis();
	actionSender = new PacketBuilder(this);
	getCharacter().setNpcsInView(new ArrayList<Npc>());
	getCharacter().setMonstersInView(new ArrayList<Monster>());
	getCharacter().setItemsInView(new ArrayList<GroundItem>());
	world.getPlayers().add(this);
    }
    
    public void checkAndStopAttack() {
	if(getCharacter().getTarget() != null)
	    getCharacter().setTarget(null);
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



    public void addProfExp(int i, int exp) {
	if(getCharacter().getProficiency_level().get(i) >= 20)
	    return;

	exp *= (int)Constants.PROF_EXP_MULTIPLIER;
	if(getCharacter().getProficiency_level().get(i) < 1)
	    getCharacter().getProficiency_level().put(i, 1);
	if(getCharacter().getProficiency().get(i) + exp > Formula.PROF_LEVEL_EXP[getCharacter().getProficiency_level().get(i) - 1]) {
	    getCharacter().getProficiency_level().put(i, getCharacter().getProficiency_level().get(i) + 1);
	    getCharacter().getProficiency().put(i, 0);
	    getActionSender().sendSystemMessage("Your Proficiency level has been improved");
	} else {
	    getCharacter().getProficiency().put(i, getCharacter().getProficiency().get(i) + exp);
	}
	getActionSender().sendProf(i, getCharacter().getProficiency_level().get(i), getCharacter().getProficiency().get(i));
    }

    public void updateNpcs() {
	for (Npc npc : World.getWorld().getNpcs()) {
	    if (npc.getMapid() == getCharacter().getMapid()) {
		if (Formula.distance(getCharacter().getX(), getCharacter().getY(), npc.getCellx(), npc.getCelly()) <= Character.VIEW_RANGE) {
		    if(!getCharacter().getNpcsInView().contains(npc)) {
			getActionSender().sendNpcSpawn(npc.getId(), npc.getCellx(), npc.getCelly(), npc.getLookface(), 1, npc.getType());
			getCharacter().getNpcsInView().add(npc);
		    }
		} else if(getCharacter().getNpcsInView().contains(npc)) {
		    getCharacter().getNpcsInView().remove(npc);
		}
	    }
	}
    }

    public void updateMonsters() {
	for (Monster monster : getMap().getMonsters().values()) {
	    if(monster.isDead())
		continue;
	    if (Formula.distance(getCharacter().getX(), getCharacter().getY(), monster.getX(), monster.getY()) <= Character.VIEW_RANGE) {
		if(!getCharacter().getMonstersInView().contains(monster)) {
		    getActionSender().sendMonsterSpawn(monster);
		    getCharacter().getMonstersInView().add(monster);
		}
	    } else if(getCharacter().getMonstersInView().contains(monster)) {
		getCharacter().getMonstersInView().remove(monster);
		// send remove to client
	    }    
	}
    }

    public void updateGroundItems() {
	for(GroundItem i : getMap().getGroundItems()) {
	    if(Formula.inView(i.getX(), i.getY(), getCharacter().getX(), getCharacter().getY())) {
		if(!getCharacter().getItemsInView().contains(i)) {
		    getCharacter().getItemsInView().add(i);
		    getActionSender().spawnGroundItem(i);
		}
	    } else if(getCharacter().getItemsInView().contains(i)) {
		getCharacter().getItemsInView().remove(i);
		getActionSender().removeGroundItem(i);
	    }    
	}
    }

    public void move(int prevX, int prevY, ByteBuffer bb) {
	getActionSender().write(ByteBuffer.wrap(bb.array().clone()));
	for (Player p : getMap().getPlayers().values()) {
	    if (p != this) {
		if (Formula.distance(prevX, prevY, p.getCharacter().getX(), p.getCharacter().getY()) <= Character.VIEW_RANGE) { // in prev view
		    if (Formula.inView(getCharacter(), p.getCharacter())) {
			p.getActionSender().write(ByteBuffer.wrap(bb.array().clone()));
			continue;
		    } else { // we have left the view
			p.getActionSender().removeEntity(this);
			getActionSender().removeEntity(p);
			continue;
		    }
		} else { // prev location is not in view
		    if (Formula.inView(getCharacter(), p.getCharacter())) { // new loc is in view
			getActionSender().sendSpawnPacket(p.getCharacter());
			p.getActionSender().sendSpawnPacket(getCharacter());
			continue;
		    }
		}
	    }
	}

	updateNpcs();
	updateMonsters();
	updateGroundItems();

    }

    public Map getMap() {
	return World.getWorld().getMaps().get(getCharacter().getMapid());
    }

    
    @Override
    public void onDeath(Entity killer) {
	
    }

    /**
     * Called when a player disconnects, gets kicked or needs to leave the
     * server
     */
    public void destroy(boolean save) {
	try {
	    if(getMap() != null) {
		for(Player p : getMap().getPlayers().values())
		    if (p != this) {
			if (Formula.inView(getCharacter(), p.getCharacter())) {
			    p.getActionSender().removeEntity(this);
			    getActionSender().removeEntity(p);
			}
		    }
		getMap().removePlayer(this);
	    }

	    try {
		world.getPlayers().remove(this);

	    } catch (ConcurrentModificationException cme) { }

	    if(save) {
		IoService.getService().saveCharacter(getCharacter());
		Constants.PLAYERS_ONLINE--;
	    }

	    if (this.getCharacter() != null && this.getCharacter().getName() != null) {
		Log.debug(this.getCharacter().getName() + " has Left the server");
	    }
	    getSession().close();
	    crypt = null;
	} catch(Exception e) {
	    Log.error(e);

	}
    }

    /**
     * Called when a player disconnects, gets kicked or needs to leave the
     * server
     */
    public void destroy() {
	destroy(true);
    }

    public void updateOthersToMe() {
	for (Player p : getPlayersInView()) {
	    getActionSender().sendSpawnPacket(p.getCharacter());
	}
    }

    public void updateMeToOthers() {
	for (Player p : getPlayersInView()) {
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

    public void setLastOption(int lastOption) {
	this.lastOption = lastOption;
    }

    public int getLastOption() {
	return lastOption;
    }

    /**
     * Dispatches a thread to run this NpcScript.
     * @param id - the NPC ID.
     */
    public void runScript(final int id) {
	final Player p = this;
	this.setLastOption(-1);
	new Thread(new Runnable() {

	    public void run() {
		script = new Script(p, StaticData.npcScripts.get(id));
	    }
	}).start();
    }

    public Script getScript() {
	return script;
    }

    public void setInterpreter(Interpreter interpreter) {
	this.interpreter = interpreter;
    }

    public Interpreter getInterpreter() {
	return interpreter;
    }
    public void setLastPing(long lastPing) {
	this.lastPing = lastPing;
    }

    public long getLastPing() {
	return lastPing;
    }
    public void setLoggedIn(boolean loggedIn) {
	this.loggedIn = loggedIn;
    }

    public boolean isLoggedIn() {
	return loggedIn;
    }
    /**
     * List of players in your view area.
     */
    private int lastOption = -1;
    private boolean loggedIn = false;
    private Script script = null;
    private long lastPing = System.currentTimeMillis();
    private Interpreter interpreter = new Interpreter();
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
