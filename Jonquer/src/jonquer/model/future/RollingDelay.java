package jonquer.model.future;

import jonquer.services.TimerService;


/**
 * This is a time varied rolling delay it will keep on repeating it's self.
 * 
 * @author xEnt
 * 
 */
public abstract class RollingDelay extends TimerService {

	/**
	 * 
	 * @param ms
	 *            - the MS to handle the delay for.
	 */
	public RollingDelay(int ms, int amount) {
		super(ms, true, amount);
	}
	
	public RollingDelay(int ms) {
		super(ms, true, -1);
	}


	public abstract void execute();

}