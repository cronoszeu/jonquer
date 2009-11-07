package jonquer.core;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import jonquer.debug.Log;
import jonquer.misc.Constants;
import jonquer.model.Packet;
import jonquer.model.Player;
import jonquer.model.World;
import jonquer.packethandler.PacketHandler;
import jonquer.services.TimerService;

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
		tick();
		processEvents();
		Thread.currentThread().sleep(Constants.GAME_LOOP_SLEEP_TIME);
	    }
	} catch (Exception e) {
	    Log.error(e);
	}
    }

    /**
     * This is where the game-engine 'ticks' all queued incoming packets are handled
     */
    private final void tick() {
	try {
	    if (System.currentTimeMillis() - lastTick > Constants.GAME_ENGINE_TICK_TIME) {
		processIncomingPackets();
		lastTick = System.currentTimeMillis();
	    }
	} catch (Exception e) {
	    Log.error(e);
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
		
			
			//TODO-fixme: Make this better.
			if(b.getData().length > 3) {
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
			   // Log.log("Invalid Packet: " + new String(b.getData())+ " Length: ?");
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
     * This handles all the Queue'd DelayedAbstractEvents events that need to be
     * processed.
     */
    private final void processEvents() {

	if (world.getTimerService().size() > 0) {
	    world.lists.addAll(world.getTimerService());
	    world.getTimerService().clear();
	}
	Iterator<TimerService> ite = world.lists.iterator();
	while(ite.hasNext()) {
	    TimerService handler = ite.next();
	    if (System.currentTimeMillis() - handler.startTime > handler.delayTime) {
		handler.execute();
		handler.rollingAmount--;
		if (!handler.rolling || handler.rollingAmount < 1) {
		    ite.remove();
		} else {
		    handler.startTime = System.currentTimeMillis();
		}
	    }
	}
	

    }

    /**
     * the Elapsed time of the last major update.
     */
    private long lastTick = 0;
    /**
     * the World instance.
     */
    public World world = World.getWorld();

}
