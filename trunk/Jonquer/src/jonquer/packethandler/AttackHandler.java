package jonquer.packethandler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import jonquer.debug.Log;
import jonquer.listeners.SkillListener;
import jonquer.misc.Formula;
import jonquer.misc.StaticData;
import jonquer.model.Entity;
import jonquer.model.Item;
import jonquer.model.Monster;
import jonquer.model.Player;
import jonquer.model.World;
import jonquer.model.future.RollingDelay;

/**
 * Added little comments around to understand it easier, as this part of the server is quite crucial.
 * @author xEnt
 *
 */
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

	Entity t = player.getMap().getMonsters().get(bb.getInt(12));
	if(t == null) {
	    t = player.getMap().getPlayers().get(bb.getInt(12));
	    if(t == null) {
		return;
	    }
	}

	if(!player.canAttack(t))
	    return;

	final Entity target = t;
	
	player.getCharacter().setTarget(target);

	/*if(type != 2) {
	    Log.log("Attack type not added: "+ type);
	    return;
	}
	if(type != 25) {
	    Log.log("Attack type not added: "+ type);
	    return;
	}*/

	/** We run a rolling delay/timer event for attacking them automatically **/
	World.getWorld().getTimerService().add(new RollingDelay(10) {
	    public void execute() {

		/** ---------------------- Fight event starts here -------------------**/

		if(player == null || target == null) {
		    stop();
		    return;
		}
		if(player.getCharacter().getTarget() != target || target.isDead()) {
		    if(!checkRestrictions(player, target))
			Log.log("checkrestriction");
		    stop();
		    return;
		}



		/** Temporary way of getting the damage, @todo: make a variable of max and minimum damage that gets changed upon equip event etc. **/
		Item righthand = player.getCharacter().getEquipment().getRight_hand();

		int damage = Formula.calcDamage(player);

		

		player.getCharacter().getTarget().dealDamage(damage);
		player.getActionSender().sendSystemMessage("HP: " + player.getCharacter().getTarget().getCurHP() + "/" + player.getCharacter().getTarget().getMaxHealth());
		/** Update players around us **/
		for(Player p : player.getMap().getPlayers().values()) {
		    if(Formula.inView(p.getCharacter(), player.getCharacter())) {
			p.getActionSender().attack(player, player.getCharacter().getTarget().getUID(), player.getCharacter().getTarget().getX(), player.getCharacter().getTarget().getY(), type, damage);
		    }
		}


		checkWeaponSkills(player);


		/** Handle what happens when they die **/
		if(player.getCharacter().getTarget().getCurHP() < 1) {
		    /** Update all players around us, of the final hit animation **/
		    for(Player p : player.getMap().getPlayers().values()) {
			if(Formula.inView(p.getCharacter(), player.getCharacter())) {
			    p.getActionSender().attack(player, player.getCharacter().getTarget().getUID(), player.getCharacter().getTarget().getX(), player.getCharacter().getTarget().getY(), 14, 0); // final hit (kill)
			}
		    }
		    player.getCharacter().getTarget().onDeath(player);
		    stop();
		}

		/** Check and remove arrows if this fight is using a bow **/
		if(righthand != null)  {
		    if(type == 25) {
			if(righthand.getDef().isTypeBow()) {
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

			}
			else  {
			    stop();
			    return;
			}
		    } 
		}
		/** Set the rolling delay, for a second or so to auto process the next hit, faster if cyclone is in use **/
		super.delayTime = Formula.rand(800, 1200);

		/** ---------------------- Fight event ENDS here -------------------**/
	    }
	});
    }


    /**
     * Here we handle everything to see if they are able to fight, restrictions they might have, check if their client data is valid etc.
     * @param player
     * @param ent
     * @return - returns true if they can continue to fight.
     */
    public boolean checkRestrictions(Player player, Entity ent) {

	if(player.getCharacter().isDead() || ent.isDead())
	    return false;

	double dist = Formula.distance(ent.getX(), ent.getY(), player.getCharacter().getX(), player.getCharacter().getY());
	Item righthand = player.getCharacter().getEquipment().getRight_hand();
	if(righthand != null)
	    if((dist > 4 && !righthand.getDef().isTypeBow()) || !righthand.getDef().isTypeBow() && dist > 15)
		return false;

	/** If they are using 2 hands, they can hit from a little further **/
	if(dist == 4 && righthand != null && !righthand.getDef().isTypeTwoHand())
	    return false;


	return true;
    }

    public void checkWeaponSkills(Player player) {
	Item righthand = player.getCharacter().getEquipment().getRight_hand();
	if(righthand != null)
	if(righthand.getDef().getWeaponType() == Formula.CLUB) {
	    if(Formula.rand(0, 100) <= StaticData.spellGroups.get(7020).getSpellDefs().get(1).getAccuracy()) {
		SkillListener sl = StaticData.skills.get(7020);
		sl.fire(player, player.getCharacter().getTarget());
	    }
	}

    }

}
