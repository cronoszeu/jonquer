package jonquer.misc;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import jonquer.model.Npc;
import jonquer.model.Shop;
import jonquer.model.def.COItemDef;
import jonquer.model.def.COMonsterDef;
import jonquer.model.def.COMonsterSpawnDef;

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
     * Stored all the monster def's.
     */
    public static HashMap<Integer, COMonsterDef> monsterDefs = new HashMap<Integer, COMonsterDef>();
    /**
     * Stored all the monster spawn def's.
     */
    public static HashMap<Integer, COMonsterSpawnDef> monsterSpawnDefs = new HashMap<Integer, COMonsterSpawnDef>();

    public static Map<Integer, Shop> shops = new HashMap<Integer, Shop>();

    public static HashSet<String> getAccounts() {
	return accounts;
    }

    public static HashSet<String> getCharacters() {
	return accounts;
    }

}
