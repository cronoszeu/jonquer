package jonquer.util;

public class Tools {

	/**
	 * Checks if an account name is in use or not.
	 * 
	 * @param s
	 *            - the character name
	 * @return - if the account exists or not
	 */
	public static boolean accountExists(String s) {
		return StaticData.getAccounts().contains(s.toLowerCase());
	}

}
