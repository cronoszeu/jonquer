package jonquer.model;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import jonquer.delay.DelayedAbstractEvent;
import jonquer.game.GameEngine;
import jonquer.game.Server;
import jonquer.packethandler.PacketHandler;

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
    public ArrayList<Player> getPlayers() {
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
    
    public void updatePosition(Player player) {
	updatePosition(player, true, null, -1, -1);
    }
    
    public void updatePosition(Player player, boolean spawn, ByteBuffer data, int prevX, int prevY) {
	for(Player p : World.getWorld().getPlayers()) {
	    if(p != player)
	    if(p.getCharacter().getMap() == player.getCharacter().getMap()) {
		p.updatePosition(player, spawn, data, prevX, prevY);
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
    private ArrayList<Player> players = new ArrayList<Player>();
    /**
     * the Single instance of this World.
     */
    public static World world;

    public static int PLAYER_INDEXES = 0;
}
