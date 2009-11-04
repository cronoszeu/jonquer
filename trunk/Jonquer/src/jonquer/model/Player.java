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
import jonquer.util.Script;
import jonquer.util.StaticData;

import org.apache.mina.common.IoSession;

import bsh.Interpreter;

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
        id = Formula.rand(0, 1000001);
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

    /*public void updateView(int prevX, int prevY) {
    for(Player p : World.getWorld().getPlayers()) {
    if(p.getCharacter().getMap() == getCharacter().getMap()) {
    if(p != this) {
    if(!Formula.inView(prevX, prevY, p.getCharacter().getX(), p.getCharacter().getY())) {
    if(Formula.inView(getCharacter(), p.getCharacter())) {
    getActionSender().sendSpawnPacket(p.getCharacter());
    }
    } else {
    if(!Formula.inView(p.getCharacter(), getCharacter())) {
    getActionSender().removeEntity(p, p.getCharacter().getX(), p.getCharacter().getY());
    }
    }
    }
    }
    }
    }*/
    public void move(int prevX, int prevY, ByteBuffer bb) {
        getActionSender().write(ByteBuffer.wrap(bb.array().clone()));
        for (Player p : World.getWorld().getPlayers()) {
            if (p != this) {
                if (p.getCharacter().getMapid() == getCharacter().getMapid()) {
                    if (Formula.distance(prevX, prevY, p.getCharacter().getX(), p.getCharacter().getY()) <= Character.VIEW_RANGE) { // in prev view
                        if (Formula.inview(getCharacter(), p.getCharacter())) {
                            p.getActionSender().write(ByteBuffer.wrap(bb.array().clone()));
                            continue;
                        } else { // we have left the view
                            p.getActionSender().removeEntity(this);
                            getActionSender().removeEntity(p);
                            continue;
                        }
                    } else { // prev location is not in view
                        if (Formula.inview(getCharacter(), p.getCharacter())) { // new loc is in view
                            getActionSender().sendSpawnPacket(p.getCharacter());
                            p.getActionSender().sendSpawnPacket(getCharacter());
                            continue;
                        }
                    }
                }
            }
        }

        for (Npc npc : StaticData.npcs) {
            if (npc.getMapid() == getCharacter().getMapid()) {
                if (Formula.distance(getCharacter().getX(), getCharacter().getY(), npc.getCellx(), npc.getCelly()) <= Character.VIEW_RANGE) {
                    getActionSender().sendNpcSpawn(npc.getId(), npc.getCellx(), npc.getCelly(), npc.getLookface(), 1, npc.getType());
                }
            }
        }
        for (Monster monster : World.getWorld().getMonsters()) {
            if (monster.getMap() == getCharacter().getMapid() && monster != null) {
                if (Formula.distance(getCharacter().getX(), getCharacter().getY(), monster.getX(), monster.getY()) <= Character.VIEW_RANGE) {
                    getActionSender().sendMonsterSpawn(monster);
                }
            }
        }
    }

    public void save() {
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(Constants.SAVED_GAME_DIRECTORY + getCharacter().getAccount() + ".cfg"));
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

        for (Player p : World.getWorld().getPlayers()) {
            if (p.getCharacter().getMapid() == getCharacter().getMapid()) {
                if (p != this) {
                    if (Formula.inview(getCharacter(), p.getCharacter())) {
                        p.getActionSender().removeEntity(this);
                        getActionSender().removeEntity(p);
                    }
                }
            }
        }

        try {
            world.getPlayers().remove(this);
        } catch (ConcurrentModificationException cme) {
        }
        save();
        if (this.getCharacter() != null && this.getCharacter().getName() != null) {
            Log.debug(this.getCharacter().getName() + " has Left the server");
        }
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
    /**
     * List of players in your view area.
     */
    private int lastOption = -1;
    private Script script = null;
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
