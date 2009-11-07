package jonquer.packethandler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import jonquer.debug.Log;
import jonquer.misc.Formula;
import jonquer.model.GroundItem;
import jonquer.model.Item;
import jonquer.model.Player;

public class PickupHandler implements PacketHandler {

    public int getPacketID() {
        return 1101;
    }

    public void handlePacket(Player player, byte[] packet) throws Exception {
        ByteBuffer bb = ByteBuffer.wrap(packet);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        int uid = bb.getInt(4);

        GroundItem it = null;
        for (GroundItem i : player.getMap().getGroundItems()) {
            if (i.getUID() == uid) {
                it = i;
                break;
            }
        }

        if (it == null) {
            return;
        }
        if (player.getCharacter().getInventory().canHold(1) || it.getDef().isMoney()) {
            if (it.getX() != player.getCharacter().getX() || it.getY() != player.getCharacter().getY()) {
                Log.hack(player.getCharacter().getName() + " tried picking up an item he's not on top of (Item: " + it.getX() + ", " + it.getY() + ") (Him: " + player.getCharacter().getX() + ", " + player.getCharacter().getY() + ")");
                return;
            }

            Item i = (Item) it;

            for (Player p : player.getMap().getPlayers().values()) {
                if (Formula.inView(it.getX(), it.getY(), p.getCharacter().getX(), p.getCharacter().getY())) {
//		    player.getActionSender().write(bb.duplicate());
                    p.getActionSender().removeGroundItem(it);
                    p.getCharacter().getItemsInView().remove(it);
                }
                if (p != player &&
                        p.getCharacter().inview(player.getCharacter())) {
                    //14 00 4D 04 PL ID PL ID 00 00 00 00 CX CX CY CY 03 00 00 00
                    ByteBuffer buf = ByteBuffer.allocate(0x14);
                    buf.order(ByteOrder.LITTLE_ENDIAN);
                    buf.putShort(0, (short) bb.limit());
                    buf.putShort(2, (short) 1101);
                    buf.putInt(4, player.getCharacter().getID());
                    buf.put(8, (byte) 0x00);
                    buf.put(9, (byte) 0x00);
                    buf.put(10, (byte) 0x00);
                    buf.put(11, (byte) 0x00);
                    buf.putShort(12, player.getCharacter().getX());
                    buf.putShort(14, player.getCharacter().getY());
                    buf.put(16, (byte) 0x03);
                    buf.put(17, (byte) 0x00);
                    buf.put(18, (byte) 0x00);
                    buf.put(19, (byte) 0x00);
                    p.getActionSender().write(buf);
                }
            }
            if (i.getDef().isMoney()) {
                player.getCharacter().setMoney(player.getCharacter().getMoney() + i.getArrowAmount());
                player.getActionSender().sendMoney();
                player.getActionSender().sendSystemMessage("You have picked up " + i.getArrowAmount() + " silvers!");
            } else {
                player.getCharacter().getInventory().addItem(i);
                player.getActionSender().sendItem(i);
                player.getActionSender().sendSystemMessage("You have picked up " + i.getDef().getName());
            }
            player.getMap().getGroundItems().remove(it);
        } else {
            player.getActionSender().sendSystemMessage("Your inventory is full");
        }
    }
}
