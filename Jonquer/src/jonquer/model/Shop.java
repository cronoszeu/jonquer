package jonquer.model;

public class Shop {
    private int id;
    private String name;
    private int type;
    private int moneyType;
    private int itemAmount;
    private int[] itemids;
    private Npc npc;

    public Shop(int id, String name, int type, int moneyType, int itemAmount,
            int[] itemids) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.moneyType = moneyType;
        this.itemAmount = itemAmount;
        this.itemids = itemids;
    }

    public boolean hasItem(int itemid) {
        for(int i = 0; i < itemids.length; i++)
            if(itemids[i] == itemid)
                return true;
        return false;
    }

    public int getId() {
        return id;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public int[] getItemids() {
        return itemids;
    }

    public int getMoneyType() {
        return moneyType;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public void setNpc(Npc npc) {
	this.npc = npc;
    }

    public Npc getNpc() {
	return npc;
    }
}
