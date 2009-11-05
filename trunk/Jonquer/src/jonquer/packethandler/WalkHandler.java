package jonquer.packethandler;

import java.awt.Point;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import jonquer.model.Npc;
import jonquer.model.Player;
import jonquer.model.World;
import jonquer.util.Formula;
import jonquer.util.StaticData;

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
        player.getCharacter().setX((short) (player.getCharacter().getX() + p.getX()));
        player.getCharacter().setY((short) (player.getCharacter().getY() + p.getY()));
        player.move(x, y, bb);
    }
}
