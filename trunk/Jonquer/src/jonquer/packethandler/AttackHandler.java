package jonquer.packethandler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import jonquer.debug.Log;
import jonquer.misc.Formula;
import jonquer.model.Entity;
import jonquer.model.Item;
import jonquer.model.Monster;
import jonquer.model.Player;
import jonquer.model.World;
import jonquer.model.future.RollingDelay;

public class AttackHandler implements PacketHandler {

    @Override
    public int getPacketID() {
	return 1022;
    }

    @Override
    public void handlePacket(final Player player, byte[] packet) throws Exception {
	final ByteBuffer bb = ByteBuffer.wrap(packet);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	final int type = bb.getInt(20);
	final Monster m = World.getWorld().getMonster(bb.getInt(12));
	if(m == null)
	    return;

	if(m.isDead())
	    return;
	Log.debug("Attack Type: " + type);
	player.getActionSender().sendSystemMessage("cq_generator ID: " + m.getSpawnDef().getId() + " / " + m.getId());
	player.getActionSender().sendSystemMessage("NPC Type: " + m.getDef().getType());

	if(type == 2 || type == 25) {
	    player.getCharacter().setTarget(m);
	    World.getWorld().getTimerService().add(new RollingDelay(25) {
		public void execute() {
		    if(player.getCharacter().getTarget() != (Entity)m || m.isDead()) {
			stop();
			return;
		    }

		    if(!checkRestrictions(player, m)){
			stop();
			return;
		    }
		    Item righthand = player.getCharacter().getEquipment().getRight_hand();
		      
		    int damage = Formula.rand(10, 15);
		    if(righthand != null)
			damage = Formula.rand(righthand.getDef().getMinDamage(), righthand.getDef().getMaxDamage());
		    if(type == 2 || type == 25) {
			m.setCurHP((m.getCurHP() - damage) <= 0 ? 0 : (m.getCurHP() - damage));
			for(Player p : player.getMap().getPlayers().values()) {
			    if(Formula.inView(p.getCharacter(), player.getCharacter())) {
				p.getActionSender().attack(player, m.getUID(), m.getX(), m.getY(), type, damage);
			    }
			}

			if(m.getCurHP() - damage < 1)
			    for(Player p : player.getMap().getPlayers().values()) {
				if(Formula.inView(p.getCharacter(), player.getCharacter())) {
				    p.getActionSender().attack(player, m.getUID(), m.getX(), m.getY(), 14, 0); // final hit (kill)
				}
			    }

			if(m.getCurHP() - damage < 1) {
			    m.onDeath(player);
			    stop();
			} 
			
			    // check & remove arrows.
			    if(righthand != null && righthand.getDef().isTypeBow() && type == 25) {
				Item arrow = player.getCharacter().getEquipment().getLeft_hand();
				if(arrow != null && arrow.getDef().isArrows())
				    if(arrow.getArrowAmount() > 1) {
					arrow.setArrowAmount(arrow.getArrowAmount() - 1);
					player.getActionSender().sendEquippedItem(arrow, Formula.LEFT_WEAPON_EQUIP_SLOT);
				    } else {
					player.getActionSender().sendSystemMessage("You have ran out of arrows");
					player.getActionSender().sendUnequipItem(arrow, Formula.LEFT_WEAPON_EQUIP_SLOT);
					player.getActionSender().removeItem(arrow);
					player.getCharacter().getEquipment().setLeft_hand(null);
					stop();
					return;
				    }
				
			    } else  {
				stop();
				return;
			    }

		    }
		    super.delayTime = Formula.rand(800, 1200);

		}
	    });
	}
    }

    public boolean checkRestrictions(Player player, Entity ent) {

	if(player.getCharacter().isDead())
	    return false;


	if(ent instanceof Monster) {
	    Monster m = (Monster)ent;
	    if(m.isDead())
		return false;


	    double dist = Formula.distance(m.getX(), m.getY(), player.getCharacter().getX(), player.getCharacter().getY());
	    Item righthand = player.getCharacter().getEquipment().getRight_hand();
	    if((dist > 4 && !righthand.getDef().isTypeBow() && righthand != null) || !righthand.getDef().isTypeBow() && dist > 15 && righthand != null)
		return false;
	    // 2h weapons can hit from a little further
	    if(dist == 4 && righthand != null && !righthand.getDef().isTypeTwoHand())
		return false;

	}
	return true;
    }

}
