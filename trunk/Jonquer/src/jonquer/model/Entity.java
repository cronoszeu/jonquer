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
    
    public void dealDamage(int damage) {
	setCurHP((getCurHP() - damage) < 0 ? 0 : (getCurHP() - damage));
	if(this instanceof Player) {
	    ((Player)this).getActionSender().vital(this.getUID(), 0, this.getCurHP());
	}
    }

}
