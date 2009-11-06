package jonquer.misc;

import java.io.File;

/**
 * Holds all important server variables, may be used in the future for some sort
 * of control panel interaction.
 * 
 * @author xEnt
 * 
 */
public class Constants {
    /**
     * the IP string for the Game host.
     */
    public static final String GAME_HOST = "127.0.0.1";
    /**
     * the IP string for the Auth host.
     */
    public static final String AUTH_HOST = "127.0.0.1";
    /**
     * the Port number for the Auth Server
     */
    public static final int AUTH_PORT = 9958;
    /**
     * the Port number for the Game Server
     */
    public static final int GAME_PORT = 5816;
    /**
     * the Directory this application launches in.
     */
    public static final String USER_DIR = System.getProperty("user.dir");
    /**
     * the Path String to the Logging text file.
     */
    public static final String LOG_FILE = USER_DIR + File.separator + "log" + File.separator + "ServerLog.txt";
    /**
     * the Status of the server.
     */
    public static boolean serverRunning = true;
    /**
     * the Time the server thread will sleep each game-engine loop in
     * milliseconds.
     */
    public static final int GAME_LOOP_SLEEP_TIME = 5;
    /**
     * the rolling milliseconds to run a major update in the game-engine loop
     */
    public static final int GAME_ENGINE_TICK_TIME = 20;
    /**
     * the amount of players this server has had since online
     */
    public static int PEAK_PLAYER_COUNT = 0;
    /**
     * Directory to the Player-Save files.
     */
    public static String SAVED_GAME_DIRECTORY = USER_DIR + File.separator + "players" + File.separator;
    /**
     * Prints extra data, helpful for debugging etc.
     */
    public static final boolean VERBOSE_DEBUG_MODE = false;
    /**
     * If a JonquerError gets thrown, enabling this will print a stack trace on the current thread.
     */
    public static final boolean THREAD_STACK_DUMP_DEBUGGING = false;
    /**
     * How many mobs the server has.
     */
    public static int MOB_COUNT = 0;
    
    
    
    /**
     * the EXP your prof is multiplied by
     */
    public static final double PROF_EXP_MULTIPLIER = 1.0;
}
