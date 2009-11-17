package jonquer.net;

public final class CONetMsgType {
    
    public static final short
            CREATE_CHARACTER = 0x3e9,
            TALK = 0x3ec,
            MOVE = 0x3ed,
            HERO_INFO = 0x3ee,
            ITEM_INFO = 0x3f0,
            ITEM_USE = 0x3f1,
            COMMAND = 0x3f2,
            CONNECTION = 0x3f4,
            MOB_SPAWN = 0x3f6,
            STRING_INFO = 0x3f7,
            WEATHER = 0x3f8,
            MOB_STATUS = 0x3f9,
            FRIEND = 0x3fb,
            TARGET = 0x3fe,
            TEAM = 0x3ff,
            STAT_POINT = 0x400,
            WEAPON_PROFICIENCY = 0x401,
            TEAM_MEMBER_INFO = 0x402,
            ITEM_GEM = 0x403,
            CRAFT = 0x404,
            LOOT_MONEY = 0x405,
            DATE_TIME = 0x409,
            LOGIN = 0x41b,
            LANGUAGE = 0x41c,
            AUTH_INFO = 0x41f,
            TRADE = 0x420,
            AUTH_CONNECT = 0x43e,
            LOOT_ITEM = 0x44d,
            WAREHOUSE = 0x44e,
            SKILL_EXPERIENCE = 0x44f,
            FLUSH_EXP = 0x450,
            TARGETS_HIT = 0x451,
            GUILD_INFO = 0x452,
            GUILD = 0x453,
            ITEM_VEND = 0x454,
            SOB_SPAWN = 0x455,
            MAP_INFO = 0x456,
            MESSAGE_BOARD = 0x457,
            GUILD_MEMBER_INFO = 0x458,
            GAMBLE = 0x459,
            NPC_SPAWN = 0x7ee,
            NPC_INIT = 0x7ef,
            NPC_COMMAND = 0x7f0,
            FRIEND_INFO = 0x7f1,
            ASSIGN_PET = 0x7f3,
            ITEM_COMPOSE = 0x7f4,
            NOBILITY_RANK = 0x7fd,
            TRADE_PARTNER = 0x7fe,
            TRADE_PARTNER_INFO = 0x7ff,
            ITEM_LOCK = 0x800,
            BROADCASTS_1 = 0x802,
            BROADCASTS_2 = 0x803,
            DONATION = 0x810,
            MENTOR_1 = 0x811,
            MENTOR_INFO = 0x812,
            MENTOR_CLAIM = 0x813,
            QUIZ_SHOW = 0x814,
            COMMAND_5105 = 0x271A;

    public static final class ChatType {

        public static final int
                TALK = 0x7d0,
                WHISPER = 0x7d1,
                ACTION = 0x7d2,
                TEAM = 0x7d3,
                GUILD = 0x7d4,
                TOP = 0x7d5,
                SPOUSE = 0x7d6,
                YELL = 0x7d8,
                FRIED = 0x7d9,
                BROADCAST = 0x7da,
                CENTER = 0x7db,
                GHOST = 0x7dd,
                SERVICE = 0x7de,
                DIALOG = 0x834,
                LOGIN_INFORMATION = 0x835,
                VENDOR_HAWK = 0x838,
                MINI_MAP = 0x83c,
                MINI_MAP_2 = 0x83d,
                FRIENDS_OFFLINE_MESSAGE = 0x83e,
                GUILD_BULLETIN = 0x83f,
                TRADE_BOARD = 0x899,
                FRIEND_BOARD = 0x89a,
                TEAM_BOARD = 0x89b,
                GUILD_BOARD = 0x89c,
                OTHERS_BOARD = 0x89d;
    }

    /** Do not let anyone instantiate this class. */
    private CONetMsgType() {}

    public static final class ItemInfoMode {

        public static final byte
                CREATE = 0x1,
                UPDATE = 0x2,
                TRADE = 0x4;

        /** Do not let anyone instantiate this class. */
        private ItemInfoMode() {}
    }

    public static final class ItemUseMode {

        public static final byte
                BUY_ITEM = 0x1,
                SELL_ITEM = 0x2,
                REMOVE_ITEM = 0x3,
                EQUIP_ITEM = 0x4,
                UPDATE_ITEM = 0x5,
                UNEQUIP_ITEM = 0x6,
                VIEW_WAREHOUSE = 0x9,
                DEPOSIT_CASH = 0xA,
                WITHDRAW_CASH = 0xB,
                DROP_MONEY = 0xC,
                DRAGON_BALL_UPGRADE = 0x13,
                METEOR_UPGRADE = 0x14,
                PING = 0x1B,
                ENCHANT_ITEM = 0x1C,
                DROP_ITEM = 0x25;

        /** Do not let anyone instantiate this class. */
        private ItemUseMode() {}
    }

    public static final class EquipmentSlot {

        public static final byte
                HELM = 0x1,
                NECKLACE = 0x2,
                ARMOR = 0x3,
                RIGHT_HAND = 0x4,
                LEFT_HAND = 0x5,
                RING = 0x6,
                TALISMAN = 0x7,
                BOOTS = 0x8,
                GARMENT = 0x9,
                ATTACK_TALISMAN = 0xA,
                DEFENSE_TALISMAN = 0xB,
                STEED = 0xC;

        /** Do not let anyone instantiate this class. */
        private EquipmentSlot() {}
    }

    public static final class CommandMode {

        public static final short
                MAP_SHOW = 0x4A,
                HOTKEYS = 0x4B,
                CONFIRM_FRIENDS = 0x4C,
                CONFIRM_PROFICIENCIES = 0x4D,
                CONFIRM_SKILLS = 0x4E,
                CHANGE_DIRECTION = 0x4F,
                ACTION = 0x51,
                PORTAL = 0x55,
                CHANGE_MAP = 0x56,
                LEVELED = 0x5C,
                END_XP_LIST = 0x5D,
                CHANGE_PK_MODE = 0x60,
                CONFIRM_GUILD = 0x61,
                MINE_SWING = 0x63,
                ENTITY_SPAWN = 0x66,
                COMPLETE_MAP_CHANGE = 0x68,
                CORRECT_COORDS = 0x6C,
                SHOP = 0x6F,
                OPEN_SHOP = 0x71,
                REMOTE_COMMANDS = 0x74,
                PICK_UP_CASH_EFFECT = 0x79,
                DIALOG = 0x7E,
                CONFIRM_LOGIN_COMPLETE = 0x82,
                SPAWN_EFFECT = 0x83,
                ENTITY_REMOVE = 0x84,
                JUMP = 0x85,
                REMOVE_WEAPON_MESH = 0x87,
                REMOVE_WEAPON_MESH_2 = 0x88;

        /** Do not let anyone instantiate this class. */
        private CommandMode() {}
    }

    public static final class StringType {

        public static final byte
                GUILD_NAME = 0x3,
                SPOUSE = 0x6,
                EFFECT = 0xA,
                GUILD_LIST = 0xB,
                VIEW_EQUIP_SPOUSE = 0x10,
                SOUND = 0x14,
                GUILD_ENEMIES = 0x15,
                GUILD_ALLIES = 0x16;

        /** Do not let anyone instantiate this class. */
        private StringType() {}
    }

    public static final class WeatherType {

        public static final byte
                NOTHING = 0x1,
                RAIN = 0x2,
                SNOW = 0x3,
                RAIN_WIND = 0x4,
                AUTUMN_LEAVES = 0x5,
                CHERRY_BLOSSOM_PETALS = 0x7,
                CHERRY_BLOSSOM_PETALS_WIND = 0x8,
                BlowingCotten = 0x9,
                ATOMS = 0xa;

        /** Do not let anyone instantiate this class. */
        private WeatherType() {}
    }

    public static final class StatusType {

        public static final byte
                HP = 0x0,
                MAX_HP = 0x1,
                MP = 0x2,
                INV_MONEY = 0x4,
                EXP = 0x5,
                PK_POINTS = 0x6,
                JOB = 0x7,
                MODIFIER = 0x8,
                STAMINA = 0x9,
                ATTRIBUTE_POINTS = 0xB,
                MODEL = 0xC,
                LEVEL = 0xD,
                SPIRIT_STAT_POINTS = 0xE,
                VITALITY_STAT_POINTS = 0xF,
                STRENGTH_STAT_POINTS = 0x10,
                AGILITY_STAT_POINTS = 0x11,
                BLESS_TIMER = 0x12,
                EXP_TIMER = 0x13,
                BLUE_TIMER = 0x14,
                BlUE_TIMER_2 = 0x15,
                STATUS_EFFECT = 0x1A,
                HAIR_STYLE = 0x1B,
                LUCKY_TIME_TIMER = 0x1D,
                INV_CONQUER_POINTS = 0x1E,
                XP_TIMER = 0x1F;

        /** Do not let anyone instantiate this class. */
        private StatusType() {}
    }

    public static final class StatusFlag {

        public static final int
                NORMAL = 0x0,
                FLASHING_NAME = 0x1,
                POISONED = 0x2,
                INVISIBLE = 0x4,
                XP_LIST = 0x10,
                DEAD = 0x20,
                TEAM_LEADER = 0x40,
                ACCURACY = 0x80,
                SHIELD = 0x100,
                STIGMA = 0x200,
                GHOST = 0x400,
                FADE_AWAY = 0x800,
                RED_NAME = 0x4000,
                BLACK_NAME = 0x8000,
                REFLECT_MELEE = 0x20000,
                SUPERMAN = 0x40000,
                BALL = 0x80000,
                BALL_2 = 0x100000,
                INVISIBILITY = 0x400000,
                CYCLONE = 0x800000,
                DODGE = 0x4000000,
                FLY = 0x8000000,
                INTENSIFY = 0x10000000,
                CAST_PRAY = 0x40000000,
                PRAYING = 0x80000000;

        /** Do not let anyone instantiate this class. */
        private StatusFlag() {}
    }

    public static final class FriendOption {

        public static final byte
                FRIED_REQUEST = 0xa,
                FRIEND_ONLINE = 0xc,
                FRIEND_DELETE = 0xe,
                SEND_FRIEND_DATA = 0xf;

        /** Do not let anyone instantiate this class. */
        private FriendOption() {}
    }

    public static final class TargetMode {

        public static final byte
                MELEE = 0x2,
                MARRIAGE_REQUEST = 0x8,
                MARRIAGE_ACCEPT = 0x9,
                KILL = 0xE,
                MAGIC = 0x15,
                ARCHER = 0x19;

        /** Do not let anyone instantiate this class. */
        private TargetMode() {}
    }

    public static final class TeamOption {

        public static final byte
                MAKE_TEAM = 0x0,
                JOIN_TEAM = 0x1,
                LEAVE_TEAM = 0x2,
                INVITE_ACCEPT = 0x3,
                INVITE = 0x4,
                ON_JOIN = 0x5,
                DISMISS_TEAM = 0x6,
                KICK = 0x7,
                FORBID_ON = 0x8,
                FORBID_OFF = 0x9,
                MEMBERS_GOLD_PICK_UP_FORBID = 0xa,
                MEMBERS_GOLD_PICK_UP_ON = 0xb,
                MEMBERS_ITEMS_PICK_UP_FORBID = 0xc,
                MEMBERS_ITEMS_PICK_UP_ON = 0xd;

        /** Do not let anyone instantiate this class. */
        private TeamOption() {}
    }

    public static final class ItemLootMode {

        public static final byte
                DROP = 0x1,
                REMOVE_SELF = 0x2,
                REMOVE_OTHER = 0x3;

        /** Do not let anyone instantiate this class. */
        private ItemLootMode() {}
    }

    public static final class GuildInfoType {

        public static final byte
                JOIN = 0x1,
                INVITE = 0x2,
                QUIT = 0x3,
                INFO = 0x6,
                ALLIED = 0x7,
                NEUTRAL = 0x8,
                ENEMIED = 0x9,
                DONATE = 0xB,
                STATUS = 0xC,
                LEAVE = 0x13;

        /** Do not let anyone instantiate this class. */
        private GuildInfoType() {}
    }

    public static final class SobSpawnType {

        /** Do not let anyone instantiate this class. */
        private SobSpawnType() {}
    }

    public static final class BoardType {

        public static final byte
                DELETE = 0x1,
                GET_LIST = 0x2,
                LIST = 0x3,
                GET_WORDS = 0x4;

        /** Do not let anyone instantiate this class. */
        private BoardType() {}
    }

    public static final class NpcOptionType {

        public static final byte
                TEXT = 0x1,
                LINK = 0x2,
                INPUT_BOX = 0x3,
                FACE = 0x4,
                LINK_2 = 0x5,
                END = 0x64;

        /** Do not let anyone instantiate this class. */
        private NpcOptionType() {}
    }

    public static final class ItemGemOption {

        /** Do not let anyone instantiate this class. */
        private ItemGemOption() {}
    }

    public static final class TradeOption {

        public static final byte
                REQUEST_TRADE = 0x1,
                BEGIN_TRADE = 0x3,
                FINISH_TRADE = 0x4,
                ACCEPT_TRADE = 0xa;

        /** Do not let anyone instantiate this class. */
        private TradeOption() {}
    }

    public static final class RebornEffect {

        public static final short
                POISON = 0xc8,
                HP = 0xc9,
                MP = 0xca,
                SHIELD = 0xcb;

        /** Do not let anyone instantiate this class. */
        private RebornEffect() {}
    }

    public static final class QuizType {

        public static final byte
                START = 0x1,
                QUESTION = 0x2,
                ANSWER = 0x3,
                INFO = 0x4,
                END = 0x5;

        /** Do not let anyone instantiate this class. */
        private QuizType() {}
    }
}
