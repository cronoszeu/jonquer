package jonquer.packethandler;

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


        byte AddX = 0;
        byte AddY = 0;
        byte Dir = (byte) (bb.getShort(8) % 8);

        switch (Dir) {
            case 0: {
                AddY = 1;
                break;
            }
            case 1: {
                AddX = -1;
                AddY = 1;
                break;
            }
            case 2: {
                AddX = -1;
                break;
            }
            case 3: {
                AddX = -1;
                AddY = -1;
                break;
            }
            case 4: {
                AddY = -1;
                break;
            }
            case 5: {
                AddX = 1;
                AddY = -1;
                break;
            }
            case 6: {
                AddX = 1;
                break;
            }
            case 7: {
                AddY = 1;
                AddX = 1;
                break;
            }
        }
        player.getCharacter().setAction(100);
        player.getCharacter().setDirection(Dir);
        int x = player.getCharacter().getX();
        int y = player.getCharacter().getY();
        player.getCharacter().setX((short) (player.getCharacter().getX() + AddX));
        player.getCharacter().setY((short) (player.getCharacter().getY() + AddY));

        World.getWorld().updatePosition(player, false, bb, x, y);

        for (Npc npc : StaticData.npcs) {
            if (npc.getMapid() == player.getCharacter().getMap()) {
                if (Formula.inFarView(player.getCharacter().getX(), player.getCharacter().getY(), npc.getCellx(), npc.getCelly())) {
                    player.getActionSender().sendNpcSpawn(npc.getId(), npc.getCellx(), npc.getCelly(), npc.getLookface(), 1, npc.getType());
                }
            }
        }
    }
}
