package jonquer.model.future;

import jonquer.model.Player;
import jonquer.services.TimerService;

/**
 * This is a single varied delayed event.
 * 
 * @author xEnt
 * 
 */
public abstract class Timer extends TimerService {

	/**
	 * 
	 * @param ms
	 *            - the MS to handle the delay for.
	 */
	public Timer(int ms, Player p) {
		super(ms, false, 0);
		player = p;
	}
	
	public Player player;

	public abstract void execute();

}
