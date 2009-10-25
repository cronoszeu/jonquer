package jonquer.delay;

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
	public Delay(int ms) {
		super(ms, false, 0);
	}

	public abstract void execute();

}
