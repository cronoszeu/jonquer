package jonquer.model;

/**
 * @author xEnt
 *
 */
public abstract class Entity {
    
    public Entity() {
	
    }
    
    /**
     * Objects using this as a superclass will inherit the abstract event when dead.
     */
    public abstract void onDeath(Entity killer);

}
