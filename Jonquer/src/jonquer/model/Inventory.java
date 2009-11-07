package jonquer.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final int MAX_SIZE = 40;
    
    private ArrayList<Item> items = new ArrayList<Item>();
    
    public ArrayList<Item> getItems() {
	return this.items;
    }
    
    public void addItem(Item i) {
	if(items.size() <= MAX_SIZE)
	    items.add(i);
    }
    
    public Item getItem(int uid) {
	for(Item i : getItems()) {
	    if(i.getUID() == uid)
		return i;
	}
	return null;
    }
    
    public boolean hasItem(int UID) {
	for(Item i : getItems()) {
	    if(i.getUID() == UID)
		return true;
	}
	return false;
    }
    
    public void removeItem(Item i) {
	items.remove(i);
    }
      
}
