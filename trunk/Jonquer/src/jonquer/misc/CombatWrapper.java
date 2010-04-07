package jonquer.misc;

import java.util.ArrayList;
import java.util.List;

import jonquer.model.Entity;
import jonquer.model.Map;
import jonquer.model.Monster;
import jonquer.model.Player;
import jonquer.model.def.COSpellDef;
import jonquer.plugins.skills.Rage;

public class CombatWrapper {

    public List<Entity> getEntitiesInRangeOther(Entity e, int range, int type) {

	Map map = null;
	if(e instanceof Monster)
	    map = ((Monster)e).getMap();
	else if(e instanceof Player)
	    map = ((Player)e).getMap();
	else throw new RuntimeException("Unknown entity?");

	List<Entity> targs = new ArrayList<Entity>();
	if(type != 1) {
	    for(Entity targetz : map.getMonsters().values()) {
		if(Formula.inRange(e, targetz, range)) {
		    targs.add(targetz);
		}
	    }
	}
	if(type != 2) {
	    for(Entity targetz : map.getPlayers().values()) {
		if(targetz == e)
		    continue;
		Player p = (Player)targetz;
		if(p.getCharacter().getFightmode() == Formula.MODE_PK)
		    if(Formula.inRange(e, targetz, range)) {
			targs.add(targetz);
		    }
	    }
	}
	return targs;
    }

    public List<Entity> getMonstersInRange(Entity e, int range) {
	return getEntitiesInRangeOther(e, range, 2);
    }

    public List<Entity> getEntitiesInRange(Entity e, int range) {
	return getEntitiesInRangeOther(e, range, 0);
    }

    public List<Entity> getPlayersInRange(Entity e, int range) {
	return getEntitiesInRangeOther(e, range, 1);
    }

    public void AOEAttack(Entity you, int spellID) {
	int spellLevel = Rage.LEVEL;
	COSpellDef sp = StaticData.spellGroups.get(spellID).getSpellDefs().get(spellLevel);

	if(you instanceof Player) {
	    Player p = (Player)you;
	    List<Integer> list = new ArrayList<Integer>();
	    List<Entity> targets = getEntitiesInRange(p, sp.getRange());
	    for(Entity targ : targets) { // calculate damages here

		int damage = Formula.rand(1, 20);
		targ.dealDamage(damage);
		list.add(damage);
		
	    }
	    for(Player pl : p.getMap().getPlayers().values())
		pl.getActionSender().magicAttack(p, p.getX(), p.getY(), spellID, sp.getLevel(), targets, list);
	}
    }

    public void checkAndHandleDeath(Player attacker, List<Entity> entities) {
	for(Entity e : entities) {
	    if(e.getCurHP() < 1) {
		if(e instanceof Monster) {
		    Monster m = (Monster)e;
		    /** Update all players around us, of the final hit animation **/
		    for(Player p : m.getMap().getPlayers().values()) {
			if(Formula.inView(p.getX(), p.getY(), m.getX(), m.getY())) {
			    p.getActionSender().attack(attacker, m.getUID(), m.getX(), m.getY(), 14, 0); // final hit (kill)
			}
		    }
		    e.onDeath(attacker);
		} else if(e instanceof Player) {
		    Player player = (Player)e;
		    for(Player p : player.getMap().getPlayers().values()) {
			if(Formula.inView(p.getCharacter(), player.getCharacter())) {
			    p.getActionSender().attack(attacker, player.getCharacter().getTarget().getUID(), player.getCharacter().getTarget().getX(), player.getCharacter().getTarget().getY(), 14, 0); // final hit (kill)
			}
		    }
		    e.onDeath(player);
		}
	    }
	}

    }

}
