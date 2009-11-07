package jonquer.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import jonquer.debug.JonquerError;
import jonquer.debug.Log;
import jonquer.misc.Formula;

public class Map {

    private int mapid;
    private byte[][] data = null;
    private HashMap<Integer, Monster> monsters;
    private HashMap<Integer, Player> players;
    private ArrayList<GroundItem> groundItems;

    public Map(int mapid, int xcoords, int ycoords) {
	this.mapid = mapid;
	data = new byte[xcoords][ycoords];
	monsters = new HashMap<Integer, Monster>();
	players = new HashMap<Integer, Player>();
	groundItems = new ArrayList<GroundItem>();
    }

    public boolean isTileBlocked(int x, int y) {
	return data[x][y] == 1;
    }

    public void removeMonster(Monster m) {
	if (getMonsters().containsValue(m)) {
	    getMonsters().remove(m.getUID());
	}
    }
    
    public void addGroundItem(GroundItem gi, Point p, int map) {
	
	getGroundItems().add(gi);
	for(Player pl : World.getWorld().getMaps().get(map).getPlayers().values()) {
	    pl.updateGroundItems();
	}
    }

    public void addMonster(Monster m) throws Exception {

	if (!getMonsters().containsValue(m)) {
	    getMonsters().put(m.getUID(), m);
	} else {
	    throw new JonquerError("Error!! Monster already exists in collection");
	}
    }

    public void removePlayer(Player p) {
	if (getPlayers().containsValue(p)) {
	    getPlayers().remove(p.getCharacter().getID());
	}
    }

    public void addPlayer(Player p) throws Exception {
	    if (!getPlayers().containsValue(p)) {
		getPlayers().put(p.getCharacter().getID(), p);
	    } else {
		throw new JonquerError("Error!! Player already exists in collection");
	    }
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

    public void setMapid(int mapid) {
	this.mapid = mapid;
    }

    public int getMapid() {
	return mapid;
    }

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
