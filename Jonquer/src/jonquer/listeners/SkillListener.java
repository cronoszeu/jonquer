package jonquer.listeners;

import jonquer.model.Entity;

/**
 * a Listener that will fire the correct skill script when a conquer skill/spell is used. (Includes a weapon skill)
 * @author xEnt
 *
 */
public interface SkillListener {
    
    public int getSkillID();
    
    public void fire(Entity you, Entity target);
    
}
