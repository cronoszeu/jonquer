package jonquer.packethandler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import jonquer.debug.Log;
import jonquer.misc.Formula;
import jonquer.model.Entity;
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
		int damage = Formula.rand(10, 15);
		if(player.getCharacter().getEquipment().getRight_hand() != null)
		    damage = Formula.rand(player.getCharacter().getEquipment().getRight_hand().getDef().getMinDamage(), player.getCharacter().getEquipment().getRight_hand().getDef().getMaxDamage());
		System.out.println("DMG: " + damage);
		if(type == 2) {
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

		}
		super.delayTime = Formula.rand(800, 1200);

	    }
	});
    }

    public boolean checkRestrictions(Player player, Entity ent) {

	if(player.getCharacter().isDead())
	    return false;

	if(ent instanceof Monster) {
	    Monster m = (Monster)ent;
	    if(m.isDead())
		return false;


	    double dist = Formula.distance(m.getX(), m.getY(), player.getCharacter().getX(), player.getCharacter().getY());
	    if(dist > 4)
		return false;
	    // 2h weapons can hit from a little further
	    if(dist == 4 && player.getCharacter().getEquipment().getRight_hand() != null && !player.getCharacter().getEquipment().getRight_hand().getDef().isTypeTwoHand())
		return false;

	}
	return true;
    }

}
