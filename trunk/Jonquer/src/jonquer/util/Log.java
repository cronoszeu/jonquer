package jonquer.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import jonquer.game.Constants;

/**
 * the Logging class to log events.
 * 
 * @author xEnt
 * 
 */
public class Log {

	/**
	 * Log's the text to file and console.
	 * 
	 * @param text
	 *            - the text to log.
	 */
	public static void log(String text) {
		cout(text);
		write(text);
	}
	
	/**
	 * When someone does something the client would not normally do without modifications.
	 * @param text
	 */
	public static void hack(String text) {
	    write("HACK: " + text);
	    cout(text);
	}

	public static void debug(String text) {
		if (Constants.VERBOSE_DEBUG_MODE)
			cout(text);
	}

	/**
	 * Prints to the console.
	 * 
	 * @param text
	 *            - the given text to display.
	 */
	private static void cout(String text) {
		System.out.printf("Logger: %1$s%n", text);
	}

	/**
	 * Logs an Error with the server
	 * 
	 * @param o
	 *            - the object given upon an exception.
	 */
	public static void error(Object o) {
		if (o instanceof Exception) {
			((Exception) o).printStackTrace();
			write(((Exception) o).getMessage());
		}
		System.out.println(o);
	}

	/**
	 * Appends a line to a file.
	 * 
	 * @param s
	 *            - the String to write.
	 */
	private static final void write(String s) {
		try {
			File f = new File(Constants.LOG_FILE);
			BufferedWriter out = new BufferedWriter(new FileWriter(f, true));
			out.write(s);
			out.newLine();
			out.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
