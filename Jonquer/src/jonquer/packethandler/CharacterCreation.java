package jonquer.packethandler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import jonquer.debug.Log;
import jonquer.model.Player;
import jonquer.util.Formula;
import jonquer.util.StaticData;
import jonquer.util.Tools;

public class CharacterCreation implements PacketHandler {

    @Override
    public int getPacketID() {
	return 1001;
    }

    @Override
    public void handlePacket(Player player, byte[] packet) throws Exception {
	ByteBuffer bb = ByteBuffer.wrap(packet);
	bb.order(ByteOrder.LITTLE_ENDIAN);
	byte job = bb.get(0x36);

	short model = (short)bb.getShort(0x34);
	int avatar = -1;
	if (model == Formula.MODEL_SMALL_BOY || model == Formula.MODEL_BIG_BOY)
	    avatar = 67;
	else if (model == Formula.MODEL_BIG_GIRL || model == Formula.MODEL_SMALL_GIRL)
	    avatar = 201;

	if(avatar == -1) {
	    Log.error("ERROR?? @ CharacterCreation avatar");
	    player.destroy();
	    return;
	}

	String name = "";
	int read = 0x14;
	while (read < 0x24 && packet[read] != 0) {
	    name = name + new String(new byte[]{packet[read]});
	    read += 1;
	}

	if(StaticData.getCharacters().contains(name.toLowerCase())) {
	    player.getActionSender().sendMessage(0xFFFFFF, Formula.CREATE_ACCOUNT_MESSAGE_TYPE, "SYSTEM", "ALLUSERS", "Character name is taken");
	} else {

	    StaticData.getCharacters().add(name.toLowerCase());
	    player.getCharacter().setName(name);
	    player.getCharacter().setLook(model);
	    player.getCharacter().setFace((short)avatar);
	    player.getCharacter().setProfession(job);

	    Formula.createCharacter(player);
	    player.getActionSender().sendMessage(0xFFFFFF, Formula.CREATE_ACCOUNT_MESSAGE_TYPE, "SYSTEM", "ALLUSERS", "ANSWER_OK");
            player.destroy();
	}
    }

}
