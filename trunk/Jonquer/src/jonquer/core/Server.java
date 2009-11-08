package jonquer.core;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.Executors;

import jonquer.debug.Log;
import jonquer.misc.Constants;
import jonquer.misc.Formula;
import jonquer.misc.StaticData;
import jonquer.misc.Tools;
import jonquer.model.Map;
import jonquer.model.Npc;
import jonquer.model.Portal;
import jonquer.model.Shop;
import jonquer.model.World;
import jonquer.model.def.COItemDef;
import jonquer.model.def.COMonsterDef;
import jonquer.model.def.COMonsterSpawnDef;
import jonquer.model.def.COItemDef.ClassRequired;
import jonquer.net.AuthConnectionHandler;
import jonquer.net.GameConnectionHandler;
import jonquer.packethandler.PacketHandler;
import jonquer.services.IoService;

import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoAcceptorConfig;
import org.apache.mina.common.ThreadModel;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;

/**
 * the Entry and initialize class.
 * 
 * @author xEnt
 * 
 */
public class Server {

    /**
     * this will initialize the server.
     */
    public void startServer() {
	try {
	    loadConfig();
	    World.getWorld().spawnNpcs();
	    Log.log("Auth Server Listening on " + Constants.AUTH_PORT);
	    Log.log("Game Server Listening on " + Constants.GAME_PORT);
	    Log.log("IO: (Character Data = " + IoService.characterIOType + ") (Server Data = " + IoService.serverIOType + ")");
	    acceptor = new SocketAcceptor(Runtime.getRuntime().availableProcessors(), Executors.newCachedThreadPool());
	    org.apache.mina.common.ByteBuffer.allocate(500);
	    IoAcceptorConfig config = new SocketAcceptorConfig();
	    config.setDisconnectOnUnbind(true);
	    config.setThreadModel(ThreadModel.MANUAL);
	    ((SocketSessionConfig) config.getSessionConfig()).setReuseAddress(true);
	    ((SocketSessionConfig) config.getSessionConfig()).setReceiveBufferSize(5000);
	    ((SocketSessionConfig) config.getSessionConfig()).setSendBufferSize(5000);
	    ((SocketSessionConfig) config.getSessionConfig()).setTcpNoDelay(false);
	    acceptor.bind(new InetSocketAddress(Constants.AUTH_HOST, Constants.AUTH_PORT), new AuthConnectionHandler(), config);
	    acceptor.bind(new InetSocketAddress(Constants.GAME_HOST, Constants.GAME_PORT), new GameConnectionHandler(), config);
	    GameEngine ge = new GameEngine();
	    World.getWorld().addInstance(ge);
	    Log.log(Constants.GAME_NAME + " v" + Constants.VERSION + " (r" + Constants.REVISION + ") is now Live!");
	    ge.loop();
	} catch (Exception e) {
	    Log.error(e);
	}
    }

    /**
     * Loads all data needed for the server
     */
    public void loadConfig() {
	long now = System.currentTimeMillis();
	Log.log("Loading Data..");
	startMeasure();
	IoService.getService().initIoPluggables();
	prepareAccounts();
	loadPacketHandlers();
	loadNpcs();
	loadMonsters();
	loadMonsterDefs();
	loadItems();
	loadScripts();
	loadMaps();
	loadPortals();
	loadShops();
	loadRevision();
	Runtime.getRuntime().gc();
	Log.log("Data loaded in " + (System.currentTimeMillis() - now) + "ms (" + finishMeasure() + "kb Allocated Memory)");
    }

    public void loadRevision() {
	try {
	    String line = "";
	    URL u = new URL("http://code.google.com/p/jonquer/source/browse/");
	    URLConnection uc = u.openConnection();
	    DataInputStream dis = new DataInputStream(uc.getInputStream());
	    String inputLine;

	    while ((inputLine = dis.readLine()) != null) {
		if(inputLine.contains("_setViewedRevision(")) {
		    String[] ss = inputLine.split("'");
		    Constants.REVISION = Integer.parseInt(ss[1]);
		}
	    }
	    dis.close();
	} catch (Exception e) {
	    Log.log("Could not load Revision.");
	}
    }

    public void loadMaps() {
	try {
	    int count = 0;
	    ArrayList<String> maps = new ArrayList<String>();
	    ArrayList<Integer> mapids = new ArrayList<Integer>();
	    FileInputStream in = new FileInputStream(Constants.USER_DIR + "/data/GameMap.dat");
	    byte buff[] = new byte[in.available()];
	    in.read(buff, 0, buff.length);
	    ByteBuffer bb = ByteBuffer.wrap(buff);
	    bb.order(ByteOrder.LITTLE_ENDIAN);
	    int amount = bb.get() & 0xff;
	    int index = 4;
	    for (int i = 0; i < amount; i++) {
		int mapid = bb.getShort(index);
		index += 4;
		int nameLen = (bb.getShort(index) & 0xff) + 2;
		index += 4;
		byte[] name = new byte[nameLen];
		System.arraycopy(bb.array(), index, name, 0, nameLen);
		maps.add(new String(name, "UTF-8"));
		mapids.add(mapid);
		index += nameLen;
		index += 4;
	    }
	    in.close();
	    int mapcount = 0;
	    for (int c = 0; c < maps.size(); c++) {
		File file = new File(Constants.USER_DIR + "/" + maps.get(c));
		if (!file.exists()) {
		    continue;
		}
		in = new FileInputStream(file);
		buff = new byte[in.available()];
		in.read(buff, 0, buff.length);
		bb = ByteBuffer.wrap(buff);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		index = 268;
		int xCount = bb.getInt(index);
		index += 4;
		int yCount = bb.getInt(index);
		index += 4;
		int mapid = mapids.get(c);
		Map map = new Map(mapid, xCount, yCount);
		for (int y = 0; y < yCount; y++) {
		    for (int x = 0; x < xCount; x++) {
			map.getData()[x][y] = bb.get(index);
			index += 6;
		    }
		    index += 4;
		}
		World.getWorld().getMaps().put(mapid, map);
		count++;
		mapcount++;
		in.close();
	    }
	    System.out.println("Loaded " + maps.size() + " Maps...");
	    maps.clear();
	    mapids.clear();

	} catch (Exception e) {
	    Log.error(e);
	}
    }

    public void loadMonsterDefs() {
	Properties properties = new Properties();
	int count = 0;
	for (File f : new File(Constants.USER_DIR + "/data/cq_generator/").listFiles()) {
	    if (f.isDirectory()) {
		continue;
	    }
	    FileInputStream in = null;
	    try {
		in = new FileInputStream(f);
		properties.load(in);
		COMonsterSpawnDef def = new COMonsterSpawnDef();
		def.setId(Integer.parseInt(properties.getProperty("id")));
		def.setMapid(Integer.parseInt(properties.getProperty("mapid")));
		def.setRespawnTime(Integer.parseInt(properties.getProperty("max_per_gen")));
		def.setMaxNpcs(Integer.parseInt(properties.getProperty("maxnpc")));
		def.setNpctype(Integer.parseInt(properties.getProperty("npctype")));
		def.setRest_secs(Integer.parseInt(properties.getProperty("rest_secs")));
		def.setBound_cx(Integer.parseInt(properties.getProperty("bound_cx")));
		def.setBound_cy(Integer.parseInt(properties.getProperty("bould_cy")));
		def.setBound_x(Integer.parseInt(properties.getProperty("bound_x")));
		def.setBound_y(Integer.parseInt(properties.getProperty("bound_y")));
		StaticData.monsterSpawnDefs.put(def.getId(), def);
		in.close();
		count++;

	    } catch (IOException e) {
		e.printStackTrace();
	    } catch (Exception e) {
		System.out.println("Skipped: " + f.getName());
		continue;
	    }
	}
	System.out.println("Loaded " + count + " MonsterSpawnDefinitions...  ");
    }

    public void loadMonsters() {
	Properties properties = new Properties();
	int count = 0;
	for (File f : new File(Constants.USER_DIR + "/data/cq_monstertype/").listFiles()) {
	    if (f.isDirectory()) {
		continue;
	    }
	    FileInputStream in = null;
	    try {
		in = new FileInputStream(f);
		properties.load(in);
		COMonsterDef def = new COMonsterDef();
		def.setId(Integer.parseInt(properties.getProperty("id")));
		def.setName(properties.getProperty("name"));
		def.setType(Integer.parseInt(properties.getProperty("type")));
		def.setLookface(Integer.parseInt(properties.getProperty("lookface")));
		def.setLife(Integer.parseInt(properties.getProperty("life")));
		def.setMana(Integer.parseInt(properties.getProperty("mana")));
		def.setMaxAttack(Integer.parseInt(properties.getProperty("attack_max")));
		def.setMinAttack(Integer.parseInt(properties.getProperty("attack_min")));
		def.setDefence(Integer.parseInt(properties.getProperty("defence")));
		def.setDexterity(Integer.parseInt(properties.getProperty("dexterity")));
		def.setDodge(Integer.parseInt(properties.getProperty("dodge")));
		def.setHelmet_type(Integer.parseInt(properties.getProperty("helmet_type")));
		def.setArmor_type(Integer.parseInt(properties.getProperty("armor_type")));
		def.setWeapon_left(Integer.parseInt(properties.getProperty("weaponl_type")));
		def.setWeapon_right(Integer.parseInt(properties.getProperty("weaponr_type")));
		def.setAttack_range(Integer.parseInt(properties.getProperty("attack_range")));
		def.setView_range(Integer.parseInt(properties.getProperty("view_range")));
		def.setEscape_life(Integer.parseInt(properties.getProperty("escape_life")));
		def.setAttack_speed(Integer.parseInt(properties.getProperty("attack_speed")));
		def.setMovement_speed(Integer.parseInt(properties.getProperty("move_speed")));
		def.setLevel(Integer.parseInt(properties.getProperty("level")));
		def.setAttack_user(Integer.parseInt(properties.getProperty("attack_user")));
		def.setDrop_money(Integer.parseInt(properties.getProperty("drop_money")));
		def.setDrop_itemtype(Integer.parseInt(properties.getProperty("drop_itemtype")));
		def.setSize_add(Integer.parseInt(properties.getProperty("size_add")));
		def.setAction(Integer.parseInt(properties.getProperty("action")));
		def.setRun_speed(Integer.parseInt(properties.getProperty("run_speed")));
		def.setDrop_garment(Integer.parseInt(properties.getProperty("drop_armet")));
		def.setDrop_necklace(Integer.parseInt(properties.getProperty("drop_necklace")));
		def.setDrop_armor(Integer.parseInt(properties.getProperty("drop_armor")));
		def.setDrop_ring(Integer.parseInt(properties.getProperty("drop_ring")));
		def.setDrop_shield(Integer.parseInt(properties.getProperty("drop_shield")));
		def.setDrop_weapon(Integer.parseInt(properties.getProperty("drop_weapon")));
		def.setDrop_shoes(Integer.parseInt(properties.getProperty("drop_shoes")));
		def.setDrop_hp(Integer.parseInt(properties.getProperty("drop_hp")));
		def.setDrop_mp(Integer.parseInt(properties.getProperty("drop_mp")));
		def.setMagic_type(Integer.parseInt(properties.getProperty("magic_type")));
		def.setMagic_def(Integer.parseInt(properties.getProperty("magic_def")));
		def.setMagic_hitrate(Integer.parseInt(properties.getProperty("magic_hitrate")));
		def.setAi_type(Integer.parseInt(properties.getProperty("ai_type")));
		def.setDefense2(Integer.parseInt(properties.getProperty("defence2")));
		def.setStc_type(Integer.parseInt(properties.getProperty("stc_type")));
		def.setAnti_monster(Integer.parseInt(properties.getProperty("anti_monster")));
		def.setExtra_battlelev(Integer.parseInt(properties.getProperty("extra_battlelev")));
		def.setExtra_exp(Integer.parseInt(properties.getProperty("extra_exp")));
		def.setExtra_damage(Integer.parseInt(properties.getProperty("extra_damage")));


		StaticData.monsterDefs.put(def.getId(), def);
		in.close();
		count++;

	    } catch (IOException e) {
		e.printStackTrace();
	    }


	}
	System.out.println("Loaded " + count + " MonsterDefinitions...  ");
    }

    /**
     * Sets up the NPC Scripts
     */
    public void loadScripts() {
	try {
	    int count = 0;
	    for (File f : new File(Constants.USER_DIR + "/src/jonquer/plugins/npcscripts/").listFiles()) {
		if (f.isDirectory() || !f.getName().endsWith(".bsh")) {
		    continue;
		}
		int id = Integer.parseInt(f.getName().substring(0, f.getName().indexOf("-")).trim());
		StaticData.npcScripts.put(id, f);
		count++;
	    }
	    System.out.println("Loaded " + count + " NPC-Scripts...  ");
	} catch (Exception e) {
	    Log.error(e);
	}

    }

    /**
     * We parse the items from the file into something useable
     */
    public void loadItems() {
	try {
	    int count = 0;
	    String line = "";
	    BufferedReader in = new BufferedReader(new FileReader(new File(Constants.USER_DIR + "/data/COItems.txt")));
	    if (!in.ready()) {
		throw new IOException();
	    }
	    while ((line = in.readLine()) != null) {
		if (count != 0) {

		    COItemDef item = new COItemDef();
		    String[] arg = line.split("=");
		    int id = Integer.parseInt(arg[0]);
		    item.setID(id);
		    String[] args = arg[1].split("-");
		    item.setName(args[0]);
		    int classid = Integer.parseInt(args[1]);
		    if (classid == 21) {
			item.setClassReq(ClassRequired.WARRIOR);
		    }
		    if (classid == 11) {
			item.setClassReq(ClassRequired.TROJAN);
		    }
		    if (classid == 45) {
			item.setClassReq(ClassRequired.ARCHER);
		    }
		    if (classid == 190) {
			item.setClassReq(ClassRequired.TAOIST);
		    }

		    if (id == 1090000 || id == 1090010 || id == 1090020 || id == 1091000 || id == 1091010 || id == 1091020) {
			item.setMoney(true);
		    }

		    item.setProfLevelReq(Integer.parseInt(args[2]));
		    item.setLevelReq(Integer.parseInt(args[3]));

		    // 4 args is just some garments with higher rarity
		    item.setReqStrength(Integer.parseInt(args[5]));
		    item.setReqAgility(Integer.parseInt(args[6]));
		    item.setPrice(Integer.parseInt(args[7]));
		    item.setMinDamage(Integer.parseInt(args[8]));
		    item.setMaxDamage(Integer.parseInt(args[9]));
		    item.setDefense(Integer.parseInt(args[10]));
		    item.setMagicDefence(Integer.parseInt(args[11]));
		    item.setMagicAttack(Integer.parseInt(args[12]));
		    item.setBonusDodge(Integer.parseInt(args[13]));
		    item.setBonusAgility(Integer.parseInt(args[14]));
		    item.setCp(Integer.parseInt(args[15]));
		    if (item.getLevelReq() > 1) {
			item.setMaxDurability(40);
		    }

		    item.setQuality(Integer.parseInt(("" + id).substring(("" + id).length() - 1))); // figured this out, and how getting quality works server-side

		    int slot = -1;
		    int wepProf = -1;
		    if (item.getID() > 110000 && item.getID() < 119999) {
			slot = 1;
		    } else if (item.getID() > 120000 && item.getID() < 129999) {
			slot = 2;
		    } else if (item.getID() > 130000 && item.getID() <= 139999) {
			slot = 3;
		    } else if (item.getID() >= 150000 && item.getID() < 159999) {
			slot = 4;
		    } else if (item.getID() >= 160000 && item.getID() < 169999) {
			slot = 5;
		    } else if (item.getID() >= 410000 && item.getID() < 499999) {
			slot = 20; // 1h
		    } else if (item.getID() >= 510000 && item.getID() < 589999) {
			slot = 21; // 2h
		    } else if (item.getID() >= 500000 && item.getID() < 599999) {
			slot = 22; // bow
		    } else if (item.getID() > 900000 && item.getID() < 991000) {
			slot = 6; // shield
		    }

		    if (slot == 20) {
			wepProf = Integer.parseInt(("" + item.getID()).substring(1, 2));
			if (item.getID() > 421000 && item.getID() < 429999) // backswords (a sub id of sword)
			{
			    wepProf = Formula.BACKSWORD;
			}
			if (item.getID() > 481000 && item.getID() < 489999) // scepter (a sub id of club)
			{
			    wepProf = Formula.SCEPTER;
			}
		    }
		    if (slot == 21) {
			wepProf = Integer.parseInt(("" + item.getID()).substring(1, 2)) + 10;
			if (item.getID() > 561000 && item.getID() < 561999) // wand (a sub id of spear)
			{
			    wepProf = Formula.WAND;
			}
		    }
		    item.setType(slot);
		    item.setWeaponType(wepProf);

		    StaticData.itemDefs.put(id, item);
		}
		count++;
	    }
	    in.close();
	    System.out.println("Loaded " + count + " ItemDefinitions...  ");
	} catch (Exception e) {
	    Log.error(e);
	}
    }

    /**
     * We load up all the classes that implement PacketHandler (which should all
     * be located in 1 folder) This allows us to simply create new packet
     * handling classes externally acting like plugins.
     */
    @SuppressWarnings("unchecked")
    public void loadPacketHandlers() {
	try {
	    int count = 0;
	    for (File f : new File(Constants.USER_DIR + "/src/jonquer/packethandler/").listFiles()) {
		if (f.getName().equals("PacketHandler.java")) {
		    continue;
		}

		if (f.isDirectory()) {
		    continue;
		}

		Class c = Class.forName("jonquer.packethandler." + f.getName().replaceAll(".java", ""));
		Object o = c.newInstance();
		if (o instanceof PacketHandler) {
		    count++;
		    PacketHandler ph = (PacketHandler) o;
		    World.getWorld().packetHandlers.put(ph.getPacketID(), ph);
		}
	    }
	    System.out.println("Loaded " + count + " PacketHandlers...   ");
	} catch (Exception e) {
	    Log.error(e);
	}
    }

    public void loadNpcs() {
	File npcDirectory = new File(Constants.USER_DIR + File.separator + "/data/cq_npc");
	Properties properties = new Properties();
	int count = 0;
	for (File file : npcDirectory.listFiles()) {
	    if (file.isDirectory()) {
		continue;
	    }

	    FileInputStream in = null;
	    try {
		in = new FileInputStream(file);
		properties.load(in);
		int id = 0000;
		int ownerid;
		int playerid;
		String name;
		int type;
		int lookface;
		int idxserver;
		int mapid;
		int cellx;
		int celly;
		int task0, task1, task2, task3, task4, task5, task6, task7;
		int data0, data1, data2, data3;
		String datastr;
		int linkid;
		int life;
		int maxlife;
		int base;
		int sort;
		int itemid;
		try {
		    id = Integer.parseInt(properties.getProperty("id"));
		    ownerid = Integer.parseInt(properties.getProperty("ownerid"));
		    playerid = Integer.parseInt(properties.getProperty("playerid"));
		    name = properties.getProperty("name");
		    type = Integer.parseInt(properties.getProperty("type"));
		    lookface = Integer.parseInt(properties.getProperty("lookface"));
		    idxserver = Integer.parseInt(properties.getProperty("idxserver"));
		    mapid = Integer.parseInt(properties.getProperty("mapid"));
		    cellx = Integer.parseInt(properties.getProperty("cellx"));
		    celly = Integer.parseInt(properties.getProperty("celly"));
		    task0 = Integer.parseInt(properties.getProperty("task0"));
		    task1 = Integer.parseInt(properties.getProperty("task1"));
		    task2 = Integer.parseInt(properties.getProperty("task2"));
		    task3 = Integer.parseInt(properties.getProperty("task3"));
		    task4 = Integer.parseInt(properties.getProperty("task4"));
		    task5 = Integer.parseInt(properties.getProperty("task5"));
		    task6 = Integer.parseInt(properties.getProperty("task6"));
		    task7 = Integer.parseInt(properties.getProperty("task7"));
		    data0 = Integer.parseInt(properties.getProperty("data0"));
		    data1 = Integer.parseInt(properties.getProperty("data1"));
		    data2 = Integer.parseInt(properties.getProperty("data2"));
		    data3 = Integer.parseInt(properties.getProperty("data3"));
		    datastr = properties.getProperty("datastr");
		    linkid = Integer.parseInt(properties.getProperty("linkid"));
		    life = Integer.parseInt(properties.getProperty("life"));
		    maxlife = Integer.parseInt(properties.getProperty("maxlife"));
		    base = Integer.parseInt(properties.getProperty("base"));
		    sort = Integer.parseInt(properties.getProperty("sort"));
		    try {
			itemid = Integer.parseInt(properties.getProperty("itemid"));
		    } catch (NumberFormatException nfe) {
			itemid = 0;
		    }

		    if (name == null || datastr == null) {
			throw new Exception("Invalid Npc data.");
		    }
		    count++;
		    World.getWorld().getNpcs().add(new Npc(id, ownerid, playerid, name, type, lookface, idxserver, mapid, cellx, celly, task0, task1, task2, task3, task4, task5, task6, task7, data0, data1, data2, data3, datastr, linkid, life, maxlife, base, sort, itemid));
		} catch (Exception ex) {
		    ex.printStackTrace();
		    Log.log("Npc " + id + " not loaded. Invalid Npc data.");
		}
	    } catch (IOException ex) {
		Log.error(ex);
	    }
	    try {
		in.close();

	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	System.out.println("Loaded " + count + " Npcs...  ");
    }

    private void loadPortals() {
	File file = new File("data" + File.separator + "Portals.ini");
	Scanner sc = null;
	int i = 0;
	try {
	    sc = new Scanner(file);
	    while (sc.hasNext()) {
		short cellx = sc.nextShort();
		short celly = sc.nextShort();
		int mapid = sc.nextInt();
		short target_cellx = sc.nextShort();
		short target_celly = sc.nextShort();
		int target_mapid = sc.nextInt();
		Portal portal = new Portal(mapid, cellx, celly,
			target_mapid, target_cellx, target_celly);
		World.getWorld().getPortals().add(portal);
		//            World.getWorld().getMaps().get(mapid).getPortals().add(portal);
		i++;
	    }
	} catch (FileNotFoundException e) {
	    Log.error(e);
	} finally {
	    sc.close();
	}
	System.out.println("Loaded " + i + " Portals...  ");
    }

    private void loadShops() {
	Properties properties = new Properties();
	File shopDirectory = new File("data" + File.separator +
		"Shop" + File.separator);
	int count = 0;
	for (File file : shopDirectory.listFiles()) {
	    if (file.isDirectory()) {
		continue;
	    }
	    FileInputStream in = null;
	    try {
		in = new FileInputStream(file);
		properties.load(in);
		int id = Integer.parseInt(properties.getProperty("ID"));
		String name = properties.getProperty("Name");
		int type = Integer.parseInt(properties.getProperty("Type"));
		int moneyType = Integer.parseInt(properties.getProperty("MoneyType"));
		int itemAmount = Integer.parseInt(properties.getProperty("ItemAmount"));
		int[] itemids = new int[itemAmount];
		for(int i = 0; i < itemAmount; i++) {
		    itemids[i] = Integer.parseInt(properties.getProperty("Item" + i));
		}
		StaticData.shops.put(id, new Shop(id, name, type, moneyType, itemAmount, itemids));
		count++;
	    } catch (Exception e) {
		Log.error(e);
	    } finally {
		try {
		    in.close();
		} catch (IOException e) {}
	    }
	}
	System.out.println("Loaded " + count + " Shops...  ");
    }

    /**
     * We start the measurement of how much memory gets used
     */
    public void startMeasure() {
	curMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    /**
     * We finish the measurement of how much memory gets used Returns the Used
     * memory (ram) from all the loaded data, in Megabytes
     */
    public long finishMeasure() {
	return ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) - curMemory) / 1000;
    }

    /**
     * We load up all the usernames allocated & store them.
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public void prepareAccounts() {
	try {
	    int count = 0;
	    File folder = new File(Constants.SAVED_GAME_DIRECTORY);
	    if (!folder.exists()) {
		folder.mkdir();
	    }
	    for (File f : folder.listFiles()) {
		if (f.isDirectory() || f == null) {
		    continue;
		}
		if (f.getName().endsWith(".cfg")) {
		    StaticData.getAccounts().add(f.getName().replaceAll(".cfg", "").toLowerCase());
		    jonquer.model.Character ch = IoService.getService().loadCharacter(f.getName().replaceAll(".cfg", "").toLowerCase());
		    if (ch != null && ch.getName() != null) {
			StaticData.getCharacters().add(ch.getName().toLowerCase());
		    }
		}
		count++;
	    }
	    System.out.println("Loaded " + count + " Accounts...  ");
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Closes the server.
     */
    public void closeServer() {
	Constants.serverRunning = false;
	acceptor.unbindAll();
	Log.log("Server Closing.. (Forced)");
    }

    /**
     * Entry point into this application.
     * 
     * @param args
     *            - the arguments given at launch (which should be none)
     */
    public static void main(String... args) {
	Server s = new Server();
	World.getWorld().addInstance(s);
	s.startServer();
    }
    /**
     * the Apache MINA Socket acceptor
     */
    private IoAcceptor acceptor;
    /**
     * a Mark of the current memory used before loading the config
     */
    private long curMemory = 0;
}
