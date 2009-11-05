package jonquer.util;

import java.awt.Point;
import java.util.Random;

import jonquer.debug.JonquerError;
import jonquer.debug.Log;
import jonquer.model.Character;
import jonquer.model.Player;
import jonquer.model.World;

public class Formula {

    /** Update type constants. */
    public static final int HITPOINTS_UPDATE_TYPE = 0;
    public static final int MAX_HITPOINTS_UPDATE_TYPE = 1;
    public static final int MANA_UPDATE_TYPE = 2;
    public static final int MAX_MANA_UPDATE_TYPE = 3;
    public static final int MONEY_UPDATE_TYPE = 4;
    public static final int EXPERIENCE_UPDATE_TYPE = 5;
    public static final int PK_POINTS_UPDATE_TYPE = 6;
    public static final int PROFESSION_UPDATE_TYPE = 7;
    public static final int BLESSED_UPDATE_TYPE = 8;
    public static final int STAMINA_UPDATE_TYPE = 9;
    public static final int STAT_POINTS_UPDATE_TYPE = 11;
    public static final int MODEL_UPDATE_TYPE = 12;
    public static final int LEVEL_UPDATE_TYPE = 13;
    public static final int SPIRIT_UPDATE_TYPE = 14;
    public static final int VITALITY_UPDATE_TYPE = 15;
    public static final int STRENGTH_UPDATE_TYPE = 16;
    public static final int AGILITY_UPDATE_TYPE = 17;
    public static final int HEAVENS_BLESSING_UPDATE_TYPE = 18;
    public static final int DOUBLE_EXPERIENCE_TIMER_UPDATE_TYPE = 19;
    public static final int CURSED_TIMER_UPDATE_TYPE = 21;
    public static final int RAISE_FLAG_UPDATE_TYPE = 26;
    public static final int LUCKY_TIME_UPDATE_TYPE = 29;
    public static final int HAIR_STYLE_UPDATE_TYPE = 31;
    
    /** Item qualities. */
    public static final int FIXED = 0;
    public static final int NORMAL1 = 3;
    public static final int NORMAL2 = 4;
    public static final int NORMAL3 = 5;
    public static final int REFINED = 6;
    public static final int UNIQUE = 7;
    public static final int ELITE = 8;
    public static final int SUPER = 9;

    /** Message type constants */
    public static final short TALK_MESSAGE_TYPE = 2000;
    public static final short WHISPER_MESSAGE_TYPE = 2001;
    public static final short TEAM_MESSAGE_TYPE = 2003;
    public static final short GUILD_MESSAGE_TYPE = 2004;
    public static final short TOP_LEFT_MESSAGE_TYPE = 2005;
    public static final short SERVICE_MESSAGE_TYPE = 2014;
    public static final short CENTER_MESSAGE_TYPE = 2011;
    public static final short CREATE_ACCOUNT_MESSAGE_TYPE = 2100;
    public static final short DIALOG_MESSAGE_TYPE = 2101;

    /** Equip slot constants. */
    public static final byte ARMET_EQUIP_SLOT = 1;
    public static final byte NECKLACE_BAG_EQUIP_SLOT = 2;
    public static final byte ARMOR_EQUIP_SLOT = 3;
    public static final byte RIGHT_WEAPON_EQUIP_SLOT = 4;
    public static final byte LEFT_WEAPON_EQUIP_SLOT = 5;
    public static final byte RING_EQUIP_SLOT = 6;
    public static final byte TALISMAN_EQUIP_SLOT = 7;
    public static final byte BOOT_EQUIP_SLOT = 8;
    public static final byte GARMENT_EQUIP_SLOT = 9;

    /** Basic character look constants. */
    public static final short LARG_FEMALE_LOOK = 2002;
    public static final short SMALL_FEMALE_LOOK = 2001;
    public static final short LARGE_MALE_LOOK = 1004;
    public static final short SMALL_MALE_LOOK = 1003;

    /** Intern character profession constants. */
    public static final int INTERN_TROJAN_PRO = 10;
    public static final int INTERN_WARRIOR_PRO = 20;
    public static final int INTERN_ARCHER_PRO = 40;
    public static final int INTERN_TAOIST_PRO = 100;
    public static final int VIEW_RADIUS = 15;
    public static final int FAR_VIEW_RADIUS = 30;

//    public static final int VIEW_RADIUS = 16;
//    public static final int FAR_VIEW_RADIUS = 30;
    /**
     * A formula to retreive max life with the given stats.
     *
     * @param vitality the character's vitality.
     * @param strength the character's strength.
     * @param agility the character's agility.
     * @param spirit the character's spirit.
     *
     * @return the max life.
     */
    public static short maxlife(short vitality, short strength, short agility, short spirit) {
        return (short) ((vitality * 24) +
                (strength * 3) +
                (agility * 3) +
                (spirit * 3));
    }

    public static boolean isTileBlocked(int map, int x, int y) {
        return World.getWorld().getMaps().get(map).isTileBlocked(x, y);
    }

    public static int rand(int min, int max) {
        if (randomNumberGenerator == null) {
            initRNG();
        }
        return randomNumberGenerator.nextInt(max - min + 1) + min;
    }

    private static Random randomNumberGenerator;
    private static void initRNG() {
        randomNumberGenerator = new Random();
    }

    public static Point dirToPoint(int dir) throws JonquerError {
        switch (dir) {

            case 0:
                return new Point(0, 1);
            case 1:
                return new Point(-1, 1);
            case 2:
                return new Point(-1, 0);
            case 3:
                return new Point(-1, -1);
            case 4:
                return new Point(0, -1);
            case 5:
                return new Point(1, -1);
            case 6:
                return new Point(1, 0);
            case 7:
                return new Point(1, 1);

            default:
                throw new JonquerError("Direction (" + dir + ") is invalid");
        }
    }

    public static void createCharacter(Player p) {
        Character character = p.getCharacter();
        if (character.getProfession() == INTERN_TROJAN_PRO) {
            character.setVitality((short) 3);
            character.setSpirit((short) 0);
            character.setAgility((short) 2);
            character.setStrength((short) 5);
        } else if (character.getProfession() == INTERN_WARRIOR_PRO) {
            character.setVitality((short) 3);
            character.setSpirit((short) 0);
            character.setAgility((short) 2);
            character.setStrength((short) 5);
        } else if (character.getProfession() == INTERN_ARCHER_PRO) {
            character.setVitality((short) 1);
            character.setSpirit((short) 0);
            character.setAgility((short) 7);
            character.setStrength((short) 2);
        } else if (character.getProfession() == INTERN_TAOIST_PRO) {
            character.setVitality((short) 3);
            character.setSpirit((short) 5);
            character.setAgility((short) 2);
            character.setStrength((short) 0);
        }

        character.setConquerPoints(0);
        character.setMoney(100);
        character.setLevel((byte) 1);
        character.setMapid(1002);
        character.setX((short) 438);
        character.setY((short) 377);
        character.setStats((short) 0);
        character.setMana((short) 0); // ill fix mana up later
        character.setSpouse("None");
        character.setLife(character.getMaxlife());

    }

    /**
     * Checks if two characters are in view of one another.
     *
     * @param a the first character.
     * @param b the second character.
     *
     * @return whether or not these two characters are in view of one another.
     */
    public static boolean inView(Character a, Character b) {
        return a.inview(b);
    }

    public static boolean inFarView(Character you, Character them) {
        return Math.abs(you.getX() - them.getX()) <= FAR_VIEW_RADIUS && Math.abs(you.getY() - them.getY()) <= FAR_VIEW_RADIUS;
    }

//    public static boolean inFarView(Character you, Character them) {
//	return Math.abs(you.getX() - them.getX()) <= FAR_VIEW_RADIUS && Math.abs(you.getY() - them.getY()) <= FAR_VIEW_RADIUS;
//    }
//
//    public static boolean inView(int oldx, int oldy, int newx, int newy) {
//	return Math.abs(oldy - newy) <= VIEW_RADIUS && Math.abs(oldx - newx) <= VIEW_RADIUS;
//    }
//
//    public static boolean inFarView(int oldx, int oldy, int newx, int newy) {
//	return Math.abs(oldy - newy) <= VIEW_RADIUS && Math.abs(oldx - newx) <= VIEW_RADIUS;
//    }
    /**
     * Gets the distance between two points.
     *
     * @param x1 the first x coordinate.
     * @param y1 the first y coordinate.
     * @param x2 the second x coordinate.
     * @param y2 the second y coordinate.
     *
     * @return the distance between these two points.
     */
    public static double distance(int x1, int y1, int x2, int y2) {
        x1 -= x2;
        y1 -= y2;
        return Math.sqrt(x1 * x1 + y1 * y1);
    }

    public static boolean inFarView(int oldx, int oldy, int newx, int newy) {
        return Math.abs(oldy - newy) <= VIEW_RADIUS && Math.abs(oldx - newx) <= VIEW_RADIUS;
    }
}
