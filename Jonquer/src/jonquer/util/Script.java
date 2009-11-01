package jonquer.util;

import java.io.File;

import jonquer.model.Inventory;
import jonquer.model.Item;
import jonquer.model.Player;

/**
 * a Script class, thats given to each script to take advantage of the simplified methods.
 * @author xEnt
 *
 */
public class Script {

    private Player player;

    public Script(Player player, File f) {
	try {
	    this.player = player;
	    player.getInterpreter().getNameSpace().importObject(this);
	    player.getInterpreter().source(f.getAbsolutePath());
	    player.getInterpreter().getNameSpace().clear();
	} catch(Exception e) {
	    Log.error(e);
	}
    }

    public void SetText(String txt) {
	player.getActionSender().sendNpcDialogPacket(txt);
	player.getActionSender().sendNpcDialogFacePacket(34);
    }

    /**
     * Beanshell Script does not support using ... (dynamic array parameters) so for ease purposes this is set up.
     * Maximum of 8 options allowed for now (we should not need any more)
     */
    public int AddOptions(String str1, String str2, String str3, String str4, String str5, String str6, String str7, String str8) {
	return SetOption(str1, str2, str3, str4, str5, str6, str7, str8);
    }
    public int AddOptions(String str1, String str2, String str3, String str4, String str5, String str6, String str7) {
	return SetOption(str1, str2, str3, str4, str5, str6, str7);
    }
    public int AddOptions(String str1, String str2, String str3, String str4, String str5, String str6) {
	return SetOption(str1, str2, str3, str4, str5, str6);
    }
    public int AddOptions(String str1, String str2, String str3, String str4, String str5) {
	return SetOption(str1, str2, str3, str4, str5);
    }
    public int AddOptions(String str1, String str2, String str3, String str4) {
	return SetOption(str1, str2, str3, str4);
    }
    public int AddOptions(String str1, String str2, String str3) {
	return SetOption(str1, str2, str3);
    }
    public int AddOptions(String str1, String str2) {
	return SetOption(str1, str2);
    }
    public int AddOptions(String str1) {
	return SetOption(str1);
    }

    public int SetOption(String... options) {
	int count = 1;
	for(String s : options) {
	    player.getActionSender().sendNpcDialogOptionPacket(s, count);
	    count++;
	}
	player.getActionSender().sendNpcDialogCompletePacket();
	player.setLastOption(-1);
	return GetOption();
    }

    public void AddItem(int itemid) {
	player.getCharacter().getInventory().addItem(new Item(itemid, 0, 0, 0, 0, 0));
	player.getActionSender().sendInventory();
    }

    public boolean CanHold(int amount) {
	return amount <= (Inventory.MAX_SIZE - player.getCharacter().getInventory().getItems().size());
    }

    public void Wait(int ms) {
	try {
	    Thread.sleep(ms);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    public int GetOption() {
	while(player.getLastOption() == -1) {
	    Wait(50);
	}
	System.out.println("found option!");
	int opt = player.getLastOption();
	System.out.println("Option choosed: " + opt);
	player.setLastOption(-1);
	return opt;
    }
}
