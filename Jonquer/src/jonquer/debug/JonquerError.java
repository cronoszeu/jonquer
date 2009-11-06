package jonquer.debug;

import jonquer.misc.Constants;

public class JonquerError extends Exception {
    
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("static-access")
    public JonquerError(String error) {
	super(error);
	Log.error(error);
	if(Constants.THREAD_STACK_DUMP_DEBUGGING)
	    Thread.currentThread().dumpStack();
    }

}
