package jonquer.packethandler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import jonquer.model.Player;
import jonquer.util.StaticData;

public class NpcHandler implements PacketHandler {

    @Override
    public int getPacketID() {
	return 2031;
    }

    @Override
    public void handlePacket(Player player, byte[] packet) throws Exception {
	ByteBuffer bb = ByteBuffer.wrap(packet);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	int npcID = bb.getInt(4);

	player.setLastOption(-1);
	if(StaticData.npcScripts.containsKey(npcID)) {
	    player.runScript(npcID);
	} else {
	    player.getActionSender().sendSystemMessage("NPC ID: " + npcID);
	}
    }

}
