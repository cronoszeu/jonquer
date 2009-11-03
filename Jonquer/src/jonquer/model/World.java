package jonquer.model;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import jonquer.delay.DelayedAbstractEvent;
import jonquer.game.GameEngine;
import jonquer.game.Server;
import jonquer.model.def.COMonsterSpawnDef;
import jonquer.packethandler.PacketHandler;
import jonquer.util.Formula;
import jonquer.util.StaticData;

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
	for(COMonsterSpawnDef spawndef : StaticData.monsterSpawnDefs.values()) {
	    int count = spawndef.getMaxNpcs();  
	    for(int i=0; i < count; i++) {
		int x;
		if(spawndef.getBound_cx() > 0)
		    x = Formula.Rand(spawndef.getBound_x(), spawndef.getBound_x() + spawndef.getBound_cx());
		else
		    x = spawndef.getBound_x();
		int y;
		if(spawndef.getBound_cy() > 0)
		    y = Formula.Rand(spawndef.getBound_y(), spawndef.getBound_y() + spawndef.getBound_cy());
		else
		    y = spawndef.getBound_y();
		getMonsters().add(new Monster(spawndef.getNpctype(), x, y, spawndef.getMapid(), spawndef.getId()));
	    }
	}
    }

    public void playerMove(Player player, ByteBuffer data, int prevX, int prevY) {
	byte[] buff = data.array().clone();
	for(Player p : getPlayers()) {
	    if(p != player)
		if(p.getCharacter().getMap() == player.getCharacter().getMap()) {
		    if(Formula.inFarView(p.getCharacter(), player.getCharacter())) {
			System.out.println(buff[5]);
			p.getActionSender().write(ByteBuffer.wrap(buff));
		    }
		}
	}
    }

    /**
     * 
     * @return - the DelayedAbstractEvent queue of awaiting entries
     */
    public ArrayList<DelayedAbstractEvent> getDelayedEventHandler() {
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

    /**
     * When the auth server sends the key off to the client, it also stores it's
     * Player object here, with the key, the client can retain it's old Player
     * object using the correct Key.
     */
    private HashMap<Integer, Player> key = new HashMap<Integer, Player>();
    /**
     * the Queue for Awaiting entries.
     */
    private ArrayList<DelayedAbstractEvent> awaitingEntries = new ArrayList<DelayedAbstractEvent>();
    /**
     * the Queue of events ready to be processed.
     */
    public ArrayList<DelayedAbstractEvent> lists = new ArrayList<DelayedAbstractEvent>();
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

    public static int PLAYER_INDEXES = 0;
}
