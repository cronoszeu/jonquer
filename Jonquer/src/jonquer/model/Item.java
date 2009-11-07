package jonquer.model;

import java.io.Serializable;

import jonquer.misc.Formula;
import jonquer.misc.StaticData;
import jonquer.model.def.COItemDef;

/**
 * This class represents an Item in Conquer.
 * @author xEnt
 *
 */
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    public Item(int id, int plus, int bless, int enchant, int soc1, int soc2) {
	this(Formula.rand(0, Integer.MAX_VALUE), id, plus, bless, enchant, soc1, soc2);
    }

    public Item(int uid, int id, int plus, int bless, int enchant, int soc1, int soc2) {
	setID(id);
	setPlus(plus);
	setBless(bless);
	setEnchant(enchant);
	setSoc1(soc1);
	setSoc2(soc2);
	if(getDef().getMaxDurability() < 1)
	    durability = -1;
	else
	    setDurability(getDef().getMaxDurability());
	setUID(uid);
    }

    public boolean hasDurability() {
	return durability != -1;
    }

    public void setID(int id) {
	this.id = id;
    }

    public int getID() {
	return id;
    }

    public COItemDef getDef() {
	return StaticData.itemDefs.get(id);
    }

    public void setPlus(int plus) {
	this.plus = plus;
    }

    public int getPlus() {
	return plus;
    }

    public void setBless(int bless) {
	this.bless = bless;
    }

    public int getBless() {
	return bless;
    }

    public void setEnchant(int enchant) {
	this.enchant = enchant;
    }

    public int getEnchant() {
	return enchant;
    }

    public void setSoc1(int soc1) {
	this.soc1 = soc1;
    }

    public int getSoc1() {
	return soc1;
    }

    public void setSoc2(int soc2) {
	this.soc2 = soc2;
    }

    public int getSoc2() {
	return soc2;
    }

    public void setUID(int uID) {
	UID = uID;
    }

    public int getUID() {
	return UID;
    }

    public void setArrowAmount(int arrowAmount) {
	this.arrowAmount = arrowAmount;
    }

    public int getArrowAmount() {
	return arrowAmount;
    }

    public void setDurability(int durability) {
	this.durability = durability;
    }

    public int getDurability() {
	return durability;
    }

    public boolean isNormal() {
	return durability == -1 && plus == 0 && bless == 0 && enchant == 0 && soc1 == 0 && soc2 == 0;
    }

    public Item clone() {
	Item i = new Item(getID(), getPlus(), getBless(), getEnchant(), getSoc1(), getSoc2());
	i.arrowAmount = arrowAmount;
	i.durability = durability;
	i.UID = UID;
	return i;
    }

    // for arrows.
    private int arrowAmount = 50;
    private int durability = -1;
    private int id = 0;
    private int plus = 0;
    private int bless = 0;
    private int enchant = 0;
    private int soc1 = 0;
    private int soc2 = 0;
    private int UID = 0;
}
