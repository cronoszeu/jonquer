package jonquer.misc;

import java.io.Serializable;
import java.util.ArrayList;

import jonquer.model.Player;

/**
 * @todo: since default List's don't implement the Serializable interface
 * we make a custom wrapper to serialize it our selves.
 * @author xEnt (UNFINISHED)
 *
 */
public class SerializedArrayList extends ArrayList<Player> implements Serializable {
    
    private static final long serialVersionUID = 1L;

    public SerializedArrayList() {
	
    }

}
