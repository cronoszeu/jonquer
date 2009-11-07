package jonquer.model;

import java.io.Serializable;
import java.util.ArrayList;

import jonquer.misc.Formula;
import jonquer.misc.SerializedArrayList;

/**
 * Represents a Character.
 * 
 * @author xEnt
 * 
 */
public class Character implements Serializable {

    public static final int VIEW_RANGE = 16;
    private static final long serialVersionUID = 1L;

    public Character(int id) {
	this.id = id;
    }

    /**
     * Tells whether or not the given character is in view of this
     * character by computing the distance, and checking if it is less than
     * or equal to the character's view range.
     *
     * @param character the character to check.
     *
     * @return whether or not the given character is in view.
     */
    public boolean inview(Character character) {
	return Formula.distance(x, y, character.x, character.y) <= VIEW_RANGE;
    }

    /**
     * Gets the maximum life of this character.
     *
     * @return the maximum life of this character
     */
    public short getMaxlife() {
	return Formula.maxlife(vitality, strength, agility, spirit);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getSpouse() {
	return spouse;
    }

    public void setSpouse(String spouse) {
	this.spouse = spouse;
    }

    public int getLook() {
	return look;
    }

    public void setLook(int look) {
	this.look = look;
    }

    public short getHairstyle() {
	return hairstyle;
    }

    public void setHairstyle(short hairstyle) {
	this.hairstyle = hairstyle;
    }

    public int getMoney() {
	return money;
    }

    public void setMoney(int money) {
	this.money = money;
    }

    public int getConquerPoints() {
	return conquerPoints;
    }

    public void setConquerPoints(int conquerPoints) {
	this.conquerPoints = conquerPoints;
    }

    public short getStrength() {
	return strength;
    }

    public void setStrength(short strength) {
	this.strength = strength;
    }

    public short getAgility() {
	return agility;
    }

    public void setAgility(short agility) {
	this.agility = agility;
    }

    public short getVitality() {
	return vitality;
    }

    public void setVitality(short vitality) {
	this.vitality = vitality;
    }

    public short getSpirit() {
	return spirit;
    }

    public void setSpirit(short spirit) {
	this.spirit = spirit;
    }

    public short getStats() {
	return stats;
    }

    public void setStats(short stats) {
	this.stats = stats;
    }

    public short getLife() {
	return life;
    }

    public void setLife(short life) {
	this.life = life;
    }

    public short getMana() {
	return mana;
    }

    public void setMana(short mana) {
	this.mana = mana;
    }

    public int getLevel() {
	return level;
    }

    public void setLevel(int level) {
	this.level = level;
    }

    public int getProfession() {
	return profession;
    }

    public void setProfession(byte profession) {
	this.profession = profession;
    }

    public byte getReborn() {
	return reborn;
    }

    public void setReborn(byte reborn) {
	this.reborn = reborn;
    }

    public int getMapid() {
	return mapid;
    }

    public void setMapid(int mapid) {
	try {
	    if(mapid != this.mapid) {
		if(World.getWorld().getMaps().get(this.mapid) != null)
		    World.getWorld().getMaps().get(this.mapid).removePlayer(ourPlayer);
		World.getWorld().getMaps().get(mapid).addPlayer(ourPlayer);
	    }
	    this.mapid = mapid;
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public short getX() {
	return x;
    }

    public void setX(short x) {
	this.x = x;
    }

    public short getY() {
	return y;
    }

    public void setDirection(byte direction) {
	this.direction = direction;
    }

    public byte getDirection() {
	return direction;
    }

    public void setY(short y) {
	this.y = y;
    }

    public int getID() {
	return id;
    }

    public void setID(int id) {
	this.id = id;
    }

    public void setAccount(String account) {
	this.account = account;
    }

    public String getAccount() {
	return account;
    }

    public void setFace(short face) {
	this.face = face;
    }

    public short getFace() {
	return face;
    }

    public void setEquipment(Equipment equipment) {
	this.equipment = equipment;
    }

    public Equipment getEquipment() {
	return equipment;
    }

    public void setWarehouse(Warehouse warehouse) {
	this.warehouse = warehouse;
    }

    public Warehouse getWarehouse() {
	return warehouse;
    }

    public void setInventory(Inventory inventory) {
	this.inventory = inventory;
    }

    public Inventory getInventory() {
	return inventory;
    }

    public void setDead(boolean dead) {
	this.dead = dead;
    }

    public boolean isDead() {
	return dead;
    }

    public void setDeathTime(long deathTime) {
	this.deathTime = deathTime;
    }

    public long getDeathTime() {
	return deathTime;
    }

    public void Experience(int experience) {
	this.experience = experience;
    }

    public int getExperience() {
	return experience;
    }

    public void setAction(int action) {
	this.action = action;
    }

    public int getAction() {
	return action;
    }
    public void setProficiency(int[] proficiency) {
	this.proficiency = proficiency;
    }

    public int[] getProficiency() {
	return proficiency;
    }
    public void setProficiency_level(int[] proficiency_level) {
	this.proficiency_level = proficiency_level;
    }

    public int[] getProficiency_level() {
	return proficiency_level;
    }
    public void setVirtuePoints(int virtuePoints) {
	this.virtuePoints = virtuePoints;
    }

    public int getVirtuePoints() {
	return virtuePoints;
    }
    public void setNpcsInView(ArrayList<Npc> npcsInView) {
	this.npcsInView = npcsInView;
    }

    public ArrayList<Npc> getNpcsInView() {
	return npcsInView;
    }
    public void setMonstersInView(ArrayList<Monster> monstersInView) {
	this.monstersInView = monstersInView;
    }

    public ArrayList<Monster> getMonstersInView() {
	return monstersInView;
    }
    public void setItemsInView(ArrayList<GroundItem> itemsInView) {
	this.itemsInView = itemsInView;
    }

    public ArrayList<GroundItem> getItemsInView() {
	return itemsInView;
    }
    /**
     * The Character's name
     */
     private String name;
    /**
     * The account name of this character
     */
     private String account;
     /**
      * The password to this account
      * 
      * @Todo: figure out what it's encrypted with
      */
     private String password;
     /**
      * This character's spouse (person they married too)
      */
     private String spouse = null;
     /**
      * This accounts Unique ID
      * 
      * @Info: marked transient because this variable does not get externalized
      */
     private transient int id;
     /**
      * Not too sure on this one, model number?
      */
     private int look;
     /**
      * Hairstyle ID i guess
      */
     private short hairstyle;
     /**
      * The picture ID when highlighting a person
      */
     private short face;
     /**
      * The amount of money this character has
      */
     private int money;
     /**
      * The amount of Conquer Points (CPs) this character has
      */
     private int conquerPoints;
     /**
      * The amount of Strength points the character has
      */
     private short strength;
     /**
      * The amount of Agility points the character has
      */
     private short agility;
     /**
      * The amount of Vitality points the character has
      */
     private short vitality;
     /**
      * The amount of Spirit points the character has
      */
     private short spirit;
     /**
      * I assume the amount of un-allocated stat points they has left to use
      */
     private short stats;
     /**
      * @Todo: figure out of this is current HP, or max HP
      */
     private short life;
     /**
      * @Todo: figure out of this is current Mana, or max Mana
      */
     private short mana;
     /**
      * The characters total level (max 130)
      */
     private int level;
     /**
      * This characters Profession
      */
     private int profession;
     /**
      * This characters Rebirth status. 0 = Normal 1 = 1st Reborn 2 = 2nd Reborn?
      */
     private byte reborn;
     /**
      * Map ID this character is on
      */
     private int mapid;
     /**
      * The X location of the character on the map
      */
     private short x;
     /**
      * The Y location of the character on the map
      */
     private short y;
     /**
      * Direction this character is facing
      */
     private byte direction = 0;
     /**
      * the amount of Experience this character has
      */
     private int experience = 0;
     /**
      * Your emotion that your using (sit, dance etc)
      */
     private transient int action = 100;
     /**
      * Is this player dead or not?
      */
     private boolean dead = false;
     /**
      * The time in milliseconds when this person died.
      */
     private transient long deathTime = 0;
     /**
      * This characters virtue points
      */
     private int virtuePoints = 0;
     /**
      * This character's inventory class
      */
     private Inventory inventory = new Inventory();
     /**
      * This character's warehouse class
      */
     private Warehouse warehouse = new Warehouse();
     /**
      * This character's equipment class
      */
     private Equipment equipment = new Equipment();
     /**
      * a reference to the class extending us.
      */
     public transient Player ourPlayer;
     /**
      * Stores the exp of proficiency
      */
     private int[] proficiency = new int[20000]; // redo do a hash map/array list
     /**
      * Stores the levels of proficiency
      */
     private int[] proficiency_level = new int[20000];
     /**
      * View'd npcs.
      */
     private transient ArrayList<Npc> npcsInView;
     /**
      * View'd monsters
      */
     private transient ArrayList<Monster> monstersInView;
     /**
      * View'd items in view (Ground Items)
      */
     private transient ArrayList<GroundItem> itemsInView;
}
