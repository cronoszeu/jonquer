package jonquer.delay;

import jonquer.model.Player;

/**
 * This is a single varied delayed event.
 * 
 * @author xEnt
 * 
 */
public abstract class Delay extends DelayedAbstractEvent {

	/**
	 * 
	 * @param ms
	 *            - the MS to handle the delay for.
	 */
	public Delay(int ms, Player p) {
		super(ms, false, 0);
		player = p;
	}
	
	public Player player;

	public abstract void execute();

}
