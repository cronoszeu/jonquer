package jonquer.model;

import java.io.Serializable;

/**
 * Represents a Character.
 * 
 * @author xEnt
 * 
 */
public class Character implements Serializable {

	private static final long serialVersionUID = 1L;

	public Character(int id) {
		this.id = id;
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

	public int getModel() {
		return model;
	}

	public void setModel(int model) {
		this.model = model;
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

	public short getHealthPoints() {
		return healthPoints;
	}

	public void setHealthPoints(short healthPoints) {
		this.healthPoints = healthPoints;
	}

	public short getManaPoints() {
		return manaPoints;
	}

	public void setManaPoints(short manaPoints) {
		this.manaPoints = manaPoints;
	}

	public byte getLevel() {
		return level;
	}

	public void setLevel(byte level) {
		this.level = level;
	}

	public byte getProfession() {
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

	public int getMap() {
		return map;
	}

	public void setMap(int map) {
		this.map = map;
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

	public void setY(short y) {
		this.y = y;
	}

	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountName() {
		return accountName;
	}
	public void setAvatar(short avatar) {
		this.avatar = avatar;
	}

	public short getAvatar() {
		return avatar;
	}
	
	/**
	 * The Character's name
	 */
	 private String name;
	/**
	 * The account name of this character
	 */
	 private String accountName;
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
	 private int model;
	 /**
	  * Hairstyle ID i guess
	  */
	 private short hairstyle;
	 /**
	  * The picture ID when highlighting a person
	  */
	 private short avatar;
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
	 private short healthPoints;
	 /**
	  * @Todo: figure out of this is current Mana, or max Mana
	  */
	 private short manaPoints;
	 /**
	  * The characters total level (max 130)
	  */
	 private byte level;
	 /**
	  * This characters Profession
	  */
	 private byte profession;
	 /**
	  * This characters Rebirth status. 0 = Normal 1 = 1st Reborn 2 = 2nd Reborn?
	  */
	 private byte reborn;
	 /**
	  * Map ID this character is on
	  */
	 private int map;
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
	 public byte direction = 0;

}
