package jonquer.packethandler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import jonquer.model.Monster;
import jonquer.model.Player;
import jonquer.model.World;

public class AttackHandler implements PacketHandler {

    @Override
    public int getPacketID() {
	return 1022;
    }

    @Override
    public void handlePacket(Player player, byte[] packet) throws Exception {
	ByteBuffer bb = ByteBuffer.wrap(packet);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	Monster m = World.getWorld().getMonster(bb.getInt(12));
	if(m != null) {
	    player.getActionSender().sendSystemMessage("cq_generator ID: " + m.getSpawnDef().getId() + " / " + m.getId());
	    player.getActionSender().sendSystemMessage("NPC Type: " + m.getDef().getType());
	}
    }

}
