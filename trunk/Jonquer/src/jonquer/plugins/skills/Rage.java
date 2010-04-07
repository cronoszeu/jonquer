package jonquer.plugins.skills;

import jonquer.listeners.SkillListener;
import jonquer.misc.CombatWrapper;
import jonquer.model.Entity;

public class Rage extends CombatWrapper implements SkillListener {

    public static int SPELL = 7020;
    public static int LEVEL = 4;
    @Override
    public void fire(Entity you, Entity target) {
	//int[] skills = {5010, 7020, 8001, 1120};
	AOEAttack(you, SPELL);
    }

    @Override
    public int getSkillID() {
	return 7020;
    }

}
