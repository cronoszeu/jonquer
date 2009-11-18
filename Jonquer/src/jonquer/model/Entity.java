package jonquer.model;

/**
 * @author xEnt
 *
 */
public abstract class Entity {
    
    public abstract void onDeath(Entity killer);
    
    public abstract boolean isDead();
    
    public abstract void setCurHP(int hp);
    
    public abstract int getCurHP();
    
    public abstract int getX();
    
    public abstract int getY();
    
    public abstract int getMaxHealth();
    
    public abstract int getUID();

}
