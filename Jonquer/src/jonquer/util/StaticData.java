package jonquer.util;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import jonquer.model.Npc;
import jonquer.model.def.COItemDef;

/**
 * Contains any type of easy-to-access data.
 * 
 * @author xEnt
 * @author s.bat
 * 
 */
public class StaticData {

    /**
     * Every account created on this server is stored here.
     */
    private static HashSet<String> accounts = new HashSet<String>();
    /**
     * Every character created on this server is stored here.
     */
    public static HashSet<String> characters = new HashSet<String>();
    /**
     * Contains Item definitions for Conquer Online. Pulled by the ItemID.
     */
    public static HashMap<Integer, COItemDef> itemDefs = new HashMap<Integer, COItemDef>();
    /**
     * a Hashmap containing all the NPC Script files, pulled by the NPC ID.
     */
    public static HashMap<Integer, File> npcScripts = new HashMap<Integer, File>();
    /**
     * All NPC definitions, @todo needs work
     */
    public static HashSet<Npc> npcs = new HashSet<Npc>();

    public static HashSet<String> getAccounts() {
	return accounts;
    }

    public static HashSet<String> getCharacters() {
	return accounts;
    }

}
