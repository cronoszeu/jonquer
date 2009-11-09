package jonquer.packethandler;

import java.awt.Point;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import jonquer.misc.Formula;
import jonquer.misc.StaticData;
import jonquer.model.Npc;
import jonquer.model.Player;
import jonquer.model.World;

public class WalkHandler implements PacketHandler {

    @Override
    public int getPacketID() {
	return 1005;
    }

    @Override
    public void handlePacket(Player player, byte[] packet) throws Exception {
	ByteBuffer bb = ByteBuffer.wrap(packet);
	bb.order(ByteOrder.LITTLE_ENDIAN);

	byte dir = (byte) (bb.getShort(8) % 8);

	player.getCharacter().setAction(100);
	player.getCharacter().setDirection(dir);
	int x = player.getCharacter().getX();
	int y = player.getCharacter().getY();
	Point p = Formula.dirToPoint(dir);

	if(p != null) {
	    player.checkAndStopAttack();
	    player.getCharacter().setX((short) (player.getCharacter().getX() + p.getX()));
	    player.getCharacter().setY((short) (player.getCharacter().getY() + p.getY()));
	    player.move(x, y, bb);
	}
    }
}
