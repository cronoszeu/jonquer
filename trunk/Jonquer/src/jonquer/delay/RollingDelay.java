package jonquer.delay;

/**
 * This is a time varied rolling delay it will keep on repeating it's self.
 * 
 * @author xEnt
 * 
 */
public abstract class RollingDelay extends DelayedAbstractEvent {

	/**
	 * 
	 * @param ms
	 *            - the MS to handle the delay for.
	 */
	public RollingDelay(int ms, int amount) {
		super(ms, true, amount);
	}

	public abstract void execute();

}