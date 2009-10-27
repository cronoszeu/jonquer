package jonquer.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;

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
	    ((SocketSessionConfig)config.getSessionConfig()).setTcpNoDelay(true); // disable nagles algorithm
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
	    for (File f : new File(System.getProperty("user.dir") + "/src/jonquer/packethandler/").listFiles()) {
		if (f.getName().equals("PacketHandler.java"))
		    continue;
		if(f.isDirectory())
		    continue;
		Class c = Class.forName("jonquer.packethandler." + f.getName().replaceAll(".java", ""));
		Object o = c.newInstance();
		if (o instanceof PacketHandler) {
		    count++;
		    PacketHandler ph = (PacketHandler) o;
		    World.getWorld().packetHandlers.put(ph.getPacketID(), ph);
		}
	    }
	    System.out.printf("Loaded " + count + " PacketHandlers...   ");
	} catch (Exception e) {
	    Log.error(e);
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
    public void prepareAccounts()  {
	try {
	    int count = 0;
	    File folder = new File(Constants.SAVED_GAME_DIRECTORY);
	    if (!folder.exists())
		folder.mkdir();
	    for (File f : folder.listFiles()) {
		if(f.isDirectory() || f == null)
		    continue;
		if (f.getName().endsWith(".cfg")) {
		    StaticData.getAccounts().add(f.getName().replaceAll(".cfg", "").toLowerCase());
		    jonquer.model.Character ch = Tools.loadCharacter(f.getName().replaceAll(".cfg", "").toLowerCase());
		    if(ch != null && ch.getName() != null)
			StaticData.getCharacters().add(ch.getName().toLowerCase());

		}
		count++;
	    }
	    System.out.printf("Loaded " + count + " Accounts...  ");
	} catch(Exception e) {
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
