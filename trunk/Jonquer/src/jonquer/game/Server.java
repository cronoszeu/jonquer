package jonquer.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

import java.util.Properties;
import jonquer.model.Npc;
import jonquer.model.World;
import jonquer.net.AuthConnectionHandler;
import jonquer.net.GameConnectionHandler;
import jonquer.packethandler.PacketHandler;
import jonquer.util.Log;
import jonquer.util.StaticData;
import jonquer.util.Tools;

import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoAcceptorConfig;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;

/**
 * the Entry and initialize class.
 * 
 * @author xEnt
 * 
 */
public class Server {

    /**
     * this will initialize the server.
     */
    public void startServer() {
        try {
            loadConfig();
            Log.log("Auth Server Listening on " + Constants.AUTH_PORT);
            Log.log("Game Server Listening on " + Constants.GAME_PORT);
            acceptor = new SocketAcceptor();
            IoAcceptorConfig config = new SocketAcceptorConfig();
            config.setDisconnectOnUnbind(true);
            ((SocketSessionConfig) config.getSessionConfig()).setReuseAddress(true);
            ((SocketSessionConfig) config.getSessionConfig()).setTcpNoDelay(true); // disable nagles algorithm
            acceptor.bind(new InetSocketAddress(Constants.AUTH_HOST, Constants.AUTH_PORT), new AuthConnectionHandler(), config);
            acceptor.bind(new InetSocketAddress(Constants.GAME_HOST, Constants.GAME_PORT), new GameConnectionHandler(), config);
            GameEngine ge = new GameEngine();
            World.getWorld().addInstance(ge);
            ge.loop();
        } catch (Exception e) {
            Log.error(e);
        }
    }

    /**
     * Loads all data needed for the server
     */
    public void loadConfig() {
        long now = System.currentTimeMillis();
        Log.log("Loading Data..");
        startMeasure();
        prepareAccounts();
        loadPacketHandlers();
        loadNpcs();
        System.out.printf("%n");
        Log.log("Data loaded in " + (System.currentTimeMillis() - now) + "ms (" + finishMeasure() + "mb Allocated Memory)");
    }

    /**
     * We load up all the classes that implement PacketHandler (which should all
     * be located in 1 folder) This allows us to simply create new packet
     * handling classes externally acting like plugins.
     */
    @SuppressWarnings("unchecked")
    public void loadPacketHandlers() {
        try {
            int count = 0;
            for (File f : new File(Constants.USER_DIR + "/src/jonquer/packethandler/").listFiles()) {
                if (f.getName().equals("PacketHandler.java")) {
                    continue;
                }
                if (f.isDirectory()) {
                    continue;
                }
                Class c = Class.forName("jonquer.packethandler." + f.getName().replaceAll(".java", ""));
                Object o = c.newInstance();
                if (o instanceof PacketHandler) {
                    count++;
                    PacketHandler ph = (PacketHandler) o;
                    World.getWorld().packetHandlers.put(ph.getPacketID(), ph);
                }
            }
            System.out.printf("Loaded " + count + " PacketHandlers...   %n");
        } catch (Exception e) {
            Log.error(e);
        }
    }

    public void loadNpcs() {
        File npcDirectory = new File(Constants.USER_DIR + File.separator + "cq_npc");
        Properties properties = new Properties();
        for (File file : npcDirectory.listFiles()) {
            FileInputStream in = null;
            try {
                in = new FileInputStream(file);
                properties.load(in);
                 int id = 0000;
                 int ownerid;
                 int playerid;
                 String name;
                 int type;
                 int lookface;
                 int idxserver;
                 int mapid;
                 int cellx;
                 int celly;
                 int task0, task1, task2, task3, task4, task5, task6, task7;
                 int data0, data1, data2, data3;
                 String datastr;
                 int linkid;
                 int life;
                 int maxlife;
                 int base;
                 int sort;
                 int itemid;
                try {
                    id = Integer.parseInt(properties.getProperty("id"));
                    ownerid = Integer.parseInt(properties.getProperty("ownerid"));
                    playerid = Integer.parseInt(properties.getProperty("playerid"));
                    name = properties.getProperty("name");
                    type = Integer.parseInt(properties.getProperty("type"));
                    lookface = Integer.parseInt(properties.getProperty("lookface"));
                    idxserver = Integer.parseInt(properties.getProperty("idxserver"));
                    mapid = Integer.parseInt(properties.getProperty("mapid"));
                    cellx = Integer.parseInt(properties.getProperty("cellx"));
                    celly = Integer.parseInt(properties.getProperty("celly"));
                    task0 = Integer.parseInt(properties.getProperty("task0"));
                    task1 = Integer.parseInt(properties.getProperty("task1"));
                    task2 = Integer.parseInt(properties.getProperty("task2"));
                    task3 = Integer.parseInt(properties.getProperty("task3"));
                    task4 = Integer.parseInt(properties.getProperty("task4"));
                    task5 = Integer.parseInt(properties.getProperty("task5"));
                    task6 = Integer.parseInt(properties.getProperty("task6"));
                    task7 = Integer.parseInt(properties.getProperty("task7"));
                    data0 = Integer.parseInt(properties.getProperty("data0"));
                    data1 = Integer.parseInt(properties.getProperty("data1"));
                    data2 = Integer.parseInt(properties.getProperty("data2"));
                    data3 = Integer.parseInt(properties.getProperty("data3"));
                    datastr = properties.getProperty("datastr");
                    linkid = Integer.parseInt(properties.getProperty("linkid"));
                    life = Integer.parseInt(properties.getProperty("life"));
                    maxlife = Integer.parseInt(properties.getProperty("maxlife"));
                    base = Integer.parseInt(properties.getProperty("base"));
                    sort = Integer.parseInt(properties.getProperty("sort"));
                    itemid = Integer.parseInt(properties.getProperty("itemid"));
                    if (name == null || datastr == null) {
                        throw new Exception("Invalid Npc data.");
                    }
                    StaticData.npcs.add(new Npc(id, ownerid, playerid, name, type, lookface, idxserver, mapid, cellx, celly, task0, task1, task2, task3, task4, task5, task6, task7, data0, data1, data2, data3, datastr, linkid, life, maxlife, base, sort, itemid));
                } catch (Exception ex) {
                    Log.log("Npc " + id + " not loaded. Invalid Npc data.");
                }
            } catch (IOException ex) {
                Log.error(ex);
            } finally {
                try {
                    in.close();
                } catch (IOException ex) {
                    Log.error(ex);
                }
            }
        }
    }

    /**
     * We start the measurement of how much memory gets used
     */
    public void startMeasure() {
        curMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    /**
     * We finish the measurement of how much memory gets used Returns the Used
     * memory (ram) from all the loaded data, in Megabytes
     */
    public double finishMeasure() {
        return (double) ((((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) - curMemory) / 1000) / 1000);
    }

    /**
     * We load up all the usernames allocated & store them.
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public void prepareAccounts() {
        try {
            int count = 0;
            File folder = new File(Constants.SAVED_GAME_DIRECTORY);
            if (!folder.exists()) {
                folder.mkdir();
            }
            for (File f : folder.listFiles()) {
                if (f.isDirectory() || f == null) {
                    continue;
                }
                if (f.getName().endsWith(".cfg")) {
                    StaticData.getAccounts().add(f.getName().replaceAll(".cfg", "").toLowerCase());
                    jonquer.model.Character ch = Tools.loadCharacter(f.getName().replaceAll(".cfg", "").toLowerCase());
                    if (ch != null && ch.getName() != null) {
                        StaticData.getCharacters().add(ch.getName().toLowerCase());
                    }

                }
                count++;
            }
            System.out.printf("Loaded " + count + " Accounts...  ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the server.
     */
    public void closeServer() {
        Constants.serverRunning = false;
        acceptor.unbindAll();
        Log.log("Server Closing.. (Forced)");
    }

    /**
     * Entry point into this application.
     * 
     * @param args
     *            - the arguments given at launch (which should be none)
     */
    public static void main(String... args) {
        Server s = new Server();
        World.getWorld().addInstance(s);
        s.startServer();
    }
    /**
     * the Apache MINA Socket acceptor
     */
    private IoAcceptor acceptor;
    /**
     * a Mark of the current memory used before loading the config
     */
    private long curMemory = 0;
}
