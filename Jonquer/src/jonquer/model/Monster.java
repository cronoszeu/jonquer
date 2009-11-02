package jonquer.model;

import jonquer.game.Constants;
import jonquer.model.def.COMonsterDef;
import jonquer.util.StaticData;

public class Monster {
    
    private int map;
    private int x;
    private int y;
    private int UID;
    private int curHP;
    private boolean dead = false;
    private long deathTime = 0;
    private int id;
    
    public Monster(int id, int x, int y, int map) {
	this.setId(id);
	this.x = x;
	this.y = y;
	this.map = map;
	this.UID = 400000 + Constants.MOB_COUNT;
	Constants.MOB_COUNT++;
    }
    
    public COMonsterDef getDef() {
	return StaticData.monsterDefs.get(getId());
    }
    
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getUID() {
        return UID;
    }
    public void setUID(int uID) {
        UID = uID;
    }
    public int getCurHP() {
        return curHP;
    }
    public void setCurHP(int curHP) {
        this.curHP = curHP;
    }
    public void setDeathTime(long deathTime) {
	this.deathTime = deathTime;
    }
    public long getDeathTime() {
	return deathTime;
    }
    public void setDead(boolean dead) {
	this.dead = dead;
    }
    public boolean isDead() {
	return dead;
    }
    public void setMap(int map) {
	this.map = map;
    }
    public int getMap() {
	return map;
    }

    public void setId(int id) {
	this.id = id;
    }

    public int getId() {
	return id;
    }

}
