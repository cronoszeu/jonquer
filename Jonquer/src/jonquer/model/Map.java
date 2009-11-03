package jonquer.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {

    private int mapID;
    
    private byte[][] data = null;
    private ArrayList<GroundItem> groundItems;

    public Map(int mapid, int x, int y) {
	data = new byte[x][y];
	groundItems = new ArrayList<GroundItem>();
	setMapID(mapid);
    }

    public void removeMonster(Monster m) {
	if(getMonsters().containsValue(m))
	    getMonsters().remove(m.getUID());
    }

    public void addMonster(Monster m) throws Exception {
	if(!getMonsters().containsValue(m))
	    getMonsters().put(m.getUID(), m);
	else
	    throw new Exception("Error!! Monster already exists in collection");
    }
    
    public void removePlayer(Player p) {
	if(getPlayers().containsValue(p))
	    getPlayers().remove(p.getCharacter().getID());
    }

    public void addPlayer(Player p) throws Exception {
	if(!getPlayers().containsValue(p))
	    getPlayers().put(p.getCharacter().getID(), p);
	else
	    throw new Exception("Error!! Player already exists in collection");
    }

    public HashMap<Integer, Monster> getMonsters() {
	return monsters;
    }

    public void setPlayers(HashMap<Integer, Player> players) {
	this.players = players;
    }

    public HashMap<Integer, Player> getPlayers() {
	return players;
    }

    public void setMapID(int mapID) {
	this.mapID = mapID;
    }

    public int getMapID() {
	return mapID;
    }

    private HashMap<Integer, Monster> monsters = new HashMap<Integer, Monster>();

    private HashMap<Integer, Player> players = new HashMap<Integer, Player>();

    public byte[][] getData() {
	return data;
    }

    public void setGroundItems(ArrayList<GroundItem> groundItems) {
	this.groundItems = groundItems;
    }

    public ArrayList<GroundItem> getGroundItems() {
	return groundItems;
    }

}
