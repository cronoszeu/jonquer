package jonquer.model;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import jonquer.core.GameEngine;
import jonquer.core.Server;
import jonquer.debug.Log;
import jonquer.misc.Formula;
import jonquer.misc.StaticData;
import jonquer.model.def.COMonsterSpawnDef;
import jonquer.packethandler.PacketHandler;
import jonquer.services.TimerService;

/**
 * the singleton World class, should be able to access most of the server from
 * here.
 * 
 * @author xEnt
 * 
 */
public class World {

    /**
     * Gets the Instance to the world.
     * 
     * @return - the only instance of the World
     */
    public static World getWorld() {
	if (world == null)
	    world = new World();
	return world;
    }

    /**
     * 
     * @return - the list of Player objects.
     */
    public List<Player> getPlayers() {
	return this.players;
    }

    /**
     * 
     * @return - the GameEngine instance.
     */
    public GameEngine getGameEngine() {
	return this.gameengine;
    }

    /**
     * 
     * @return - the Server instance
     */
    public Server getServer() {
	return this.server;
    }

    /**
     * Sets the instances to the class variables held in here.
     * 
     * @param inst
     *            - the Given instance.
     */
    public void addInstance(Object inst) {
	if (inst instanceof Server)
	    server = (Server) inst;
	if (inst instanceof GameEngine)
	    gameengine = (GameEngine) inst;
    }

    public Monster getMonster(int uid) {
	for(Monster m : getMonsters()) {
	    if(m.getUID() == uid) {
		return m;
	    }
	}
	return null;
    }

    public void spawnNpcs() {
	try {
	    for(COMonsterSpawnDef spawndef : StaticData.monsterSpawnDefs.values()) {
		int count = spawndef.getMaxNpcs();  
		for(int i=0; i < count; i++) {
		    int x;
		    if(spawndef.getBound_cx() > 0)
			x = Formula.rand(spawndef.getBound_x(), spawndef.getBound_x() + spawndef.getBound_cx());
		    else
			x = spawndef.getBound_x();
		    int y;
		    if(spawndef.getBound_cy() > 0)
			y = Formula.rand(spawndef.getBound_y(), spawndef.getBound_y() + spawndef.getBound_cy());
		    else
			y = spawndef.getBound_y();
		   
		    if(getMaps().get(spawndef.getMapid()) == null)
			continue;
		    Monster m = new Monster(spawndef.getNpctype(), x, y, spawndef.getMapid(), spawndef.getId());
		    getMonsters().add(m);
		    getMaps().get(spawndef.getMapid()).addMonster(m);
		}
	    }
	} catch(Exception e) {
	    Log.error(e);
	}
    }


    /**
     * 
     * @return - the TimerService queue of awaiting entries
     */
    public ArrayList<TimerService> getDelayedEventHandler() {
	return this.awaitingEntries;
    }

    /**
     * 
     * @return - the key map
     */
    public HashMap<Integer, Player> getKeyPlayers() {
	return this.key;
    }

    public void setMonsters(List<Monster> monsters) {
	this.monsters = monsters;
    }

    public List<Monster> getMonsters() {
	return monsters;
    }

    public void setMaps(HashMap<Integer, Map> maps) {
	this.maps = maps;
    }

    public HashMap<Integer, Map> getMaps() {
	return maps;
    }

    public void setNpcs(HashSet<Npc> npcs) {
	this.npcs = npcs;
    }

    public HashSet<Npc> getNpcs() {
	return npcs;
    }

    /**
     * When the auth server sends the key off to the client, it also stores it's
     * Player object here, with the key, the client can retain it's old Player
     * object using the correct Key.
     */
    private HashMap<Integer, Player> key = new HashMap<Integer, Player>();
    /**
     * the Queue for Awaiting entries.
     */
    private ArrayList<TimerService> awaitingEntries = new ArrayList<TimerService>();
    /**
     * the Queue of events ready to be processed.
     */
    public ArrayList<TimerService> lists = new ArrayList<TimerService>();
    /**
     * HashMap holding all the IDS and classes that implement PacketHandler.
     */
    public HashMap<Integer, PacketHandler> packetHandlers = new HashMap<Integer, PacketHandler>();
    /**
     * Contains all the Maps loaded on the server.
     */
    private HashMap<Integer, Map> maps = new HashMap<Integer, Map>();
    /**
     * the Server object.
     */
    private Server server;
    /**
     * the GameEngine object
     */
    private GameEngine gameengine;
    /**
     * holds all the active Players in the world.
     */
    private List<Player> players = Collections.synchronizedList(new ArrayList<Player>());
    /**
     * holds all the active Monsters in the world.
     */
    private List<Monster> monsters = Collections.synchronizedList(new ArrayList<Monster>());
    /**
     * the Single instance of this World.
     */
    public static World world;
    /**
     * All NPC definitions, @todo needs work
     */
    private HashSet<Npc> npcs = new HashSet<Npc>();

    public static int PLAYER_INDEXES = 0;
}
