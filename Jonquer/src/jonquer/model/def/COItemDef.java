package jonquer.model.def;

import jonquer.misc.Formula;

/**
 * Represents a Conquer Item-Definition.
 * @author xEnt
 *
 */
public class COItemDef {

    public int getPrice() {
	return price;
    }
    public void setPrice(int price) {
	this.price = price;
    }
    public int getCp() {
	return cp;
    }
    public void setCp(int cp) {
	this.cp = cp;
    }

    public int getMinDamage() {
	return minDamage;
    }
    public void setMinDamage(int minDamage) {
	this.minDamage = minDamage;
    }
    public int getMaxDamage() {
	return maxDamage;
    }
    public void setMaxDamage(int maxDamage) {
	this.maxDamage = maxDamage;
    }
    public int getMaxDurability() {
	return maxDurability;
    }
    public void setMaxDurability(int maxDurability) {
	this.maxDurability = maxDurability;
    }
    public int getLevelReq() {
	return levelReq;
    }
    public void setLevelReq(int levelReq) {
	this.levelReq = levelReq;
    }
    public int getProfLevelReq() {
	return profLevelReq;
    }
    public void setProfLevelReq(int profLevelReq) {
	this.profLevelReq = profLevelReq;
    }
    public int getProfID() {
	return profID;
    }
    public void setProfID(int profID) {
	this.profID = profID;
    }
    public void setID(int iD) {
	ID = iD;
    }
    public int getID() {
	return ID;
    }

    public void setName(String name) {
	this.name = name;
    }
    public String getName() {
	return name;
    }

    public void setClassReq(ClassRequired classReq) {
	this.classReq = classReq;
    }
    public ClassRequired getClassReq() {
	return classReq;
    }

    public void setReqStrength(int reqStrength) {
	this.reqStrength = reqStrength;
    }
    public int getReqStrength() {
	return reqStrength;
    }

    public void setReqAgility(int reqAgility) {
	this.reqAgility = reqAgility;
    }
    public int getReqAgility() {
	return reqAgility;
    }


    public void setDefense(int defense) {
	this.defense = defense;
    }
    public int getDefense() {
	return defense;
    }

    public void setMagicAttack(int magicAttack) {
	this.magicAttack = magicAttack;
    }
    public int getMagicAttack() {
	return magicAttack;
    }

    public void setMagicDefence(int magicDefence) {
	this.magicDefence = magicDefence;
    }
    public int getMagicDefence() {
	return magicDefence;
    }

    public void setBonusAgility(int bonusAgility) {
	this.bonusAgility = bonusAgility;
    }
    public int getBonusAgility() {
	return bonusAgility;
    }

    public void setBonusDodge(int bonusDodge) {
	this.bonusDodge = bonusDodge;
    }
    public int getBonusDodge() {
	return bonusDodge;
    }

    public boolean isArrows() {
	return ID == 1050002 || ID== 1050001 || ID == 1050000;
    }

    public void setQuality(int quality) {
	this.quality = quality;
    }
    public int getQuality() {
	return quality;
    }

    public boolean isQualitySuper() {
	return getID() == Formula.SUPER;
    }

    public boolean isQualityElite() {
	return getID() == Formula.ELITE;
    }

    public boolean isQualityUnique() {
	return getID() == Formula.UNIQUE;
    }

    public boolean isQualityRefined() {
	return getID() == Formula.REFINED;
    }

    public boolean isQualityNormal() {
	return getID() == Formula.NORMAL1 || getID() == Formula.NORMAL2 || getID() == Formula.NORMAL3;
    }

    public boolean isQualityFixed() {
	return getID() == Formula.FIXED;
    }

    public boolean isTypeBow() {
	return type == 22; 
    }
    
    public boolean isTypeOneHand() {
	return type == 22 || type == 20;
    }
    
    public boolean isTypeTwoHand() {
	return type == 21;
    }
    
   public boolean isTypeShield() {
       return type == 6;
   }
    
    public boolean isTypeHelmet() {
	return type == 1;
    }
    
    public boolean isTypeNecklace() {
	return type == 2;
    }
    
    public boolean isTypeArmor() {
	return type == 3;
    }
    
    public boolean isTypeRing() {
	return type == 4;
    }
    public boolean isTypeBoots() {
	return type == 5;
    }

    public void setWeaponType(int weaponType) {
	this.weaponType = weaponType;  
    }

    public int getWeaponType() {
	return weaponType;
    }
    
    public void setType(int type) {
	this.type = type;
    }
    
    public int getType() {
	return this.type;
    }

    public void setMoney(boolean money) {
	this.money = money;
    }
    public boolean isMoney() {
	return money;
    }
    
    public boolean isHealthPotion() {
	return potionType == 1;
    }
    
    public boolean isManaPotion() {
	return potionType == 2;
    }
    
    public void setPotionType(byte type) {
	this.potionType = type;
    }

    public void setPotionRecovery(int potionRecovery) {
	this.potionRecovery = potionRecovery;
    }
    public int getPotionRecovery() {
	return potionRecovery;
    }

    private boolean money = false;
    private int potionRecovery = -1;
    private byte potionType = -1;
    private int weaponType = 0;
    private int type = 0;
    private int quality = -1;
    private String name = null;
    private int price = 0;
    private int cp = 0;
    private int ID = 0;
    private int bonusDodge = 0;
    private int bonusAgility = 0;
    private int defense = 0;
    private int magicAttack = 0;
    private int magicDefence = 0;
    private int reqStrength = 0;
    private int reqAgility = 0;
    private int minDamage = 0;
    private int maxDamage = 0;
    private int maxDurability = 0;
    private int levelReq = 0;
    private int profLevelReq = 0;
    private int profID = 0;
    private ClassRequired classReq = ClassRequired.ALL;

    public static enum ClassRequired {TROJAN, ARCHER, WARRIOR, TAOIST, ALL, NONE}
}