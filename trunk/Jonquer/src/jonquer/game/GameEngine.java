package jonquer.game;

import java.nio.ByteBuffer;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;

import jonquer.delay.DelayedAbstractEvent;
import jonquer.model.Packet;
import jonquer.model.Player;
import jonquer.model.World;
import jonquer.net.StaticPacketBuilder;
import jonquer.packethandler.AuthLogin;
import jonquer.packethandler.GameLogin;
import jonquer.packethandler.PacketHandler;
import jonquer.util.Crypto;
import jonquer.util.Log;

/**
 * the GameEngine of the entire server.
 * 
 * @author xEnt
 * 
 */
public class GameEngine {

    /**
     * the Game-Engine loop
     */
    @SuppressWarnings("static-access")
    public void loop() {
	try {
	    while (Constants.serverRunning) {
		majorUpdate();
		minorUpdate();
		processEvents();
		Thread.currentThread().sleep(Constants.GAME_LOOP_SLEEP_TIME);
	    }
	} catch (Exception e) {
	    Log.error(e);
	}
    }

    /**
     * a more Important update, that will be processed faster than others.
     */
    private final void majorUpdate() {
	try {
	    if (System.currentTimeMillis() - lastMajor > Constants.MAJOR_UPDATE_TIME_LOOP) {
		processIncomingPackets();
		processOutgoingPackets();
		lastMajor = System.currentTimeMillis();
	    }
	} catch (Exception e) {
	    Log.error(e);
	}
    }

    /**
     * an Update that does not need to be updated too fast.
     */
    private final void minorUpdate() {
	if (System.currentTimeMillis() - lastMinor > Constants.MINOR_UPDATE_TIME_LOOP) {
	    lastMinor = System.currentTimeMillis();
	}
    }

    /**
     * Process all the Incoming Packets that were Queue'd and ready to be
     * handled.
     */
    private final void processIncomingPackets() {
	try {
	    for (Player p : world.getPlayers()) {
		boolean needsDestroy = false;
		if (p != null && p.getIncomingPackets().size() > 0) {

		    Iterator<Packet> i = p.getIncomingPackets().iterator();
		    while (i.hasNext()) {

			if (p.crypt == null) {
			    World.getWorld().getPlayers().remove(p);
			    needsDestroy = true;
			    break;
			}

			Packet b = i.next();
			p.crypt.decrypt(b.getData());
			//TODO-fixme: Make this better.
			if(b.getData().length > 1) {
			    int packetID = (b.getData()[3] << 8) | (b.getData()[2] & 0xff);

			    if (!World.getWorld().packetHandlers.containsKey(packetID)) {
				Log.log("Unhandled Packet: " + packetID + " Length: " + b.getData().length);
			    } else {
				PacketHandler ph = World.getWorld().packetHandlers.get(packetID);
				try {
				    ph.handlePacket(p, b.getData());
				} catch(Exception e) {
				    e.printStackTrace();
				    p.destroy();
				}
			    }
			} else {
			    Log.log("Invalid Packet: " + new String(b.getData())+ " Length: ?");
			}

			i.remove();
		    }
		    if (needsDestroy)
			continue;
		}
	    }

	} catch(ConcurrentModificationException cme) {

	} catch (Exception e) {
	    e.printStackTrace();
	} 
    }

    /**
     * Processes all the Packets queued up and sends them.
     */
    private final void processOutgoingPackets() {

	for (Player p : world.getPlayers()) {
	    boolean needsDestroy = false;
	    if (p != null && p.getOutgoingPackets().size() > 0) {
		Iterator<Packet> i = p.getOutgoingPackets().iterator();
		while (i.hasNext()) {
		    if (p.crypt == null) {
			World.getWorld().getPlayers().remove(p);
			needsDestroy = true;
			break;
		    }
		    Packet b = i.next();
		    p.crypt.encrypt(b.getData());
		    ByteBuffer buff = ByteBuffer.wrap(b.getData());
		    p.getSession().write(buff);
		    i.remove();
		}
		if (needsDestroy)
		    continue;
	    }
	}
    }

    /**
     * This handles all the Queue'd DelayedAbstractEvents events that need to be
     * processed.
     */
    private final void processEvents() {

	if (world.getDelayedEventHandler().size() > 0) {
	    world.lists.addAll(world.getDelayedEventHandler());
	    world.getDelayedEventHandler().clear();
	}
	for (DelayedAbstractEvent handler : world.lists) {
	    if (System.currentTimeMillis() - handler.startTime > handler.delayTime) {
		handler.execute();
		handler.rollingAmount--;
		if (!handler.rolling || handler.rollingAmount < 1) {
		    world.lists.remove(handler);
		} else {
		    handler.startTime = System.currentTimeMillis();
		}
	    }
	}

    }

    /**
     * the Elapsed time of the last major update.
     */
    private long lastMajor = 0;
    /**
     * the Elapsed time of the last minor update.
     */
    private long lastMinor = 0;
    /**
     * the World instance.
     */
    public World world = World.getWorld();

}
