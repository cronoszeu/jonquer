package jonquer.model;

public class GroundItem extends Item {
     
    public GroundItem(int uid, int id, int plus, int bless, int enchant, int soc1, int soc2, int x, int y, int map) {
	super(uid, id, plus, bless, enchant, soc1, soc2);
	setX(x);
	setY(y);
	setMap(map);
    }
    
    public void setX(int x) {
	this.x = x;
    }
    public int getX() {
	return x;
    }

    public void setY(int y) {
	this.y = y;
    }

    public int getY() {
	return y;
    }

    public void setDropedTime(long dropedTime) {
	this.dropedTime = dropedTime;
    }

    public long getDropedTime() {
	return dropedTime;
    }

    public void setMap(int map) {
	this.map = map;
    }

    public int getMap() {
	return map;
    }

    private int x;
    private int y;
    private int map;
    private long dropedTime = System.currentTimeMillis();

}
