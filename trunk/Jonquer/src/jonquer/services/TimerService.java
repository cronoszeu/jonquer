package jonquer.services;

/**
 * this TimerService is a type of custom 'TimerService' for handling abstracted delays on
 * a single thread (the main game-engine thread)
 * 
 * @author xEnt
 * 
 */
public abstract class TimerService {

	/**
	 * 
	 * @param ms
	 *            - the specified delay time in milliseconds.
	 * @param roll
	 *            - boolean giving the state of rolling or not.
	 */
	public TimerService(int ms, boolean roll, int amount) {
		delayTime = ms;
		startTime = System.currentTimeMillis();
		rolling = roll;
		rollingAmount = amount;
	}

	public abstract void execute();
	
	public void stop() {
	    rolling = false;
	    rollingAmount = 0;
	}

	/**
	 * the starting time in milliseconds
	 */
	public long startTime;
	/**
	 * the specified delay time
	 */
	public int delayTime = 0;
	/**
	 * is this a rolling delay or not?
	 */
	public boolean rolling = false;
	/**
	 * the amount of rolls this Event has left.
	 */
	public int rollingAmount = 0;

}