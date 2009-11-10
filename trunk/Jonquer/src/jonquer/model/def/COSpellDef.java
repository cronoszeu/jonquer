package jonquer.model.def;

public class COSpellDef {
    
    private String name;
    private int ID;
    private int type;
    private int offensive;
    private int ground;
    private int multiTarget;
    private int level;
    private int mana;
    private int baseDamage;
    private int stamina;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getID() {
        return ID;
    }
    public void setID(int iD) {
        ID = iD;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getOffensive() {
        return offensive;
    }
    public void setOffensive(int offensive) {
        this.offensive = offensive;
    }
    public int getGround() {
        return ground;
    }
    public void setGround(int ground) {
        this.ground = ground;
    }
    public int getMultiTarget() {
        return multiTarget;
    }
    public void setMultiTarget(int multiTarget) {
        this.multiTarget = multiTarget;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public int getMana() {
        return mana;
    }
    public void setMana(int mana) {
        this.mana = mana;
    }
    public int getBaseDamage() {
        return baseDamage;
    }
    public void setBaseDamage(int baseDamage) {
        this.baseDamage = baseDamage;
    }
    public int getStamina() {
        return stamina;
    }
    public void setStamina(int stamina) {
        this.stamina = stamina;
    }
    public int getRange() {
        return range;
    }
    public void setRange(int range) {
        this.range = range;
    }
    public int getAccuracy() {
        return accuracy;
    }
    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }
    private int range = 0;
    private int accuracy = 100;

}
