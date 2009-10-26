package jonquer.util;

import jonquer.model.Player;
import jonquer.model.Character;

public class Formula {

    public static final short TALK_MESSAGE_TYPE = 2000;
    public static final short WHISPER_MESSAGE_TYPE = 2001;
    public static final short TEAM_MESSAGE_TYPE = 2003;
    public static final short GUILD_MESSAGE_TYPE = 2004;
    public static final short TOP_LEFT_MESSAGE_TYPE = 2005;
    public static final short SERVICE_MESSAGE_TYPE = 2014;
    public static final short CENTER_MESSAGE_TYPE = 2011;
    public static final short CREATE_ACCOUNT_MESSAGE_TYPE = 2100;
    public static final short DIALOG_MESSAGE_TYPE = 2101;


    public static final short MODEL_BIG_GIRL = 2002;
    public static final short MODEL_SMALL_GIRL = 2001;
    public static final short MODEL_BIG_BOY = 1004;
    public static final short MODEL_SMALL_BOY = 1003;


    public static final int JOB_INTERN_TROJAN = 10;
    public static final int JOB_INTERN_WARRIOR = 20;
    public static final int JOB_INTERN_ARCHER = 40;
    public static final int JOB_INTERN_TAOIST = 100;

    /**
     * Calculates the Total Health.
     * @param vit
     * @param str
     * @param agi
     * @param spi
     * @return
     */
    public static int getHealth(short vit, short str, short agi, short spi) {
	return (vit * 24) + (str * 3) + (agi * 3) + (spi * 3);
    }

    public static void createCharacter(Player p) {
	if(p.getCharacter().getProfession() == JOB_INTERN_TROJAN) {
	    p.getCharacter().setVitality((short)3);
	    p.getCharacter().setSpirit((short)0);
	    p.getCharacter().setAgility((short)2);
	    p.getCharacter().setStrength((short)5);
	} else if(p.getCharacter().getProfession() == JOB_INTERN_WARRIOR) {
	    p.getCharacter().setVitality((short)3);
	    p.getCharacter().setSpirit((short)0);
	    p.getCharacter().setAgility((short)2);
	    p.getCharacter().setStrength((short)5);
	} else if(p.getCharacter().getProfession() == JOB_INTERN_ARCHER) {
	    p.getCharacter().setVitality((short)1);
	    p.getCharacter().setSpirit((short)0);
	    p.getCharacter().setAgility((short)7);
	    p.getCharacter().setStrength((short)2);
	} else if(p.getCharacter().getProfession() == JOB_INTERN_TAOIST) {
	    p.getCharacter().setVitality((short)3);
	    p.getCharacter().setSpirit((short)5);
	    p.getCharacter().setAgility((short)2);
	    p.getCharacter().setStrength((short)0);
	} 

	p.getCharacter().setConquerPoints(0);
	p.getCharacter().setMoney(100);
	p.getCharacter().setLevel((byte)1);
	p.getCharacter().setMap(1002);
	p.getCharacter().setX((short)438);
	p.getCharacter().setY((short)377);
	p.getCharacter().setStats((short)0);
	p.getCharacter().setManaPoints((short)0); // ill fix mana up later
	p.getCharacter().setSpouse("None");
	p.getCharacter().setHealthPoints((short)getHealth(p.getCharacter().getVitality(), p.getCharacter().getStrength(), p.getCharacter().getSpirit(), p.getCharacter().getAgility()));

    }

    /**
     * Checks if another character is in view of this character.
     * @param you
     * @param them
     * @return
     */
    public static boolean inView(Character you, Character them) {
	return Math.abs(you.getX() - them.getX()) <= 17 && Math.abs(you.getY() - them.getY()) <= 17;
    }
    
    public static boolean inView(int oldx, int oldy, int newx, int newy) {
	return Math.abs(oldy - newy) <= 17 && Math.abs(oldx - newx) <= 17;
    }


}
