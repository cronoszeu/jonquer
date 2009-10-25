package jonquer.util;

import java.util.HashSet;

/**
 * Contains any type of easy-to-access data.
 * 
 * @author xEnt
 * 
 */
public class StaticData {

    private static HashSet<String> accounts = new HashSet<String>();

    public static HashSet<String> characters = new HashSet<String>();

    public static HashSet<String> getAccounts() {
	return accounts;
    }

    public static HashSet<String> getCharacters() {
	return accounts;
    }

}
