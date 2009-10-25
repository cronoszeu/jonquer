package jonquer.delay;

/**
 * this DelayedAbstractEvent is a type of custom 'Timer' for handling delays on
 * a single thread (the main game-engine thread)
 * 
 * @author xEnt
 * 
 */
public abstract class DelayedAbstractEvent {

    /**
     * 
     * @param ms
     *            - the specified delay time in milliseconds.
     * @param roll
     *            - boolean giving the state of rolling or not.
     */
    public DelayedAbstractEvent(int ms, boolean roll, int amount) {
	delayTime = ms;
	startTime = System.currentTimeMillis();
	rolling = roll;
	rollingAmount = amount;
    }

    public abstract void execute();

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