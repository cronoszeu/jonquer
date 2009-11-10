package jonquer.model;

import jonquer.debug.Log;
import jonquer.misc.Constants;
import jonquer.misc.Formula;
import jonquer.misc.StaticData;
import jonquer.model.def.COMonsterDef;
import jonquer.model.def.COMonsterSpawnDef;
import jonquer.model.future.Timer;

public class Monster extends Entity {

    private int map;
    private int x;
    private int y;
    private int UID;
    private int curHP;
    private boolean dead = false;
    private long deathTime = 0;
    private int id;
    private int spawnDefId = 0;

    public Monster(int id, int x, int y, int map, int spawnDefId) {
	this.spawnDefId = spawnDefId;
	this.setId(id);
	this.x = x;
	this.y = y;
	this.map = map;
	this.UID = 400000 + Constants.MOB_COUNT;
	Constants.MOB_COUNT++;
	World.getWorld().getMonsters().add(this);
    }

    public COMonsterSpawnDef getSpawnDef() {
	return StaticData.monsterSpawnDefs.get(spawnDefId);
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

    public Map getMap() {
	return World.getWorld().getMaps().get(getSpawnDef().getMapid());
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
    public int getMapID() {
	return map;
    }

    public void setId(int id) {
	this.id = id;
    }

    public int getId() {
	return id;
    }

    public void setSpawnDefId(int spawnDefId) {
	this.spawnDefId = spawnDefId;
    }

    public int getSpawnDefId() {
	return spawnDefId;
    }

    public Monster getMonster() {
	return this;
    }

    public static int MOB_DEATHS = 0;
    @Override
    public void onDeath(Entity killer) {
	getMap().removeMonster(this);
	MOB_DEATHS++;
	for(Player p : getMap().getPlayers().values()) {
	    if(Formula.inView(p.getCharacter().getX(), p.getCharacter().getY(), getX(), getY())) {
		p.getActionSender().fadeMonster(this);
		p.getCharacter().getMonstersInView().remove(this);
		p.updateMonsters();

	    }
	}
	((Player)killer).getCharacter().setTarget(null);

	// set them for respawn.
	World.getWorld().getTimerService().add(new Timer(getSpawnDef().getRespawnTime() * 1000, null) {
	    public void execute() {
		try {
		    getMap().addMonster(getMonster());
		    setCurHP(getDef().getLife());

		    setDead(false);
		    int x;
		    
		    do {
			
			if(getSpawnDef().getBound_cx() > 0)
			    x = Formula.rand(getSpawnDef().getBound_x(), getSpawnDef().getBound_x() + getSpawnDef().getBound_cx());
			else
			    x = getSpawnDef().getBound_x();
			int y;
			if(getSpawnDef().getBound_cy() > 0)
			    y = Formula.rand(getSpawnDef().getBound_y(), getSpawnDef().getBound_y() + getSpawnDef().getBound_cy());
			else
			    y = getSpawnDef().getBound_y();
			
		    } while(Formula.isTileBlocked(getMap().getMapid(), x, y));

		    setX(x);
		    setY(y);
		    for(Player p : getMap().getPlayers().values()) {
			if(Formula.inView(p.getCharacter().getX(), p.getCharacter().getY(), getMonster().getX(), getMonster().getY())) {
			    p.getActionSender().removeMonster(getMonster());
			    p.getCharacter().getMonstersInView().add(getMonster());

			    p.getActionSender().sendMonsterSpawn(getMonster());
			    p.getActionSender().respawnMonster(getMonster());
			    p.updateMonsters();	 
			}
		    }
		} catch (Exception e) {
		    Log.error(e);
		}
	    }
	});
    }

}
