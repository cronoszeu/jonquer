package jonquer.packethandler;

import jonquer.model.Player;

/**
 * the Interface to handle each Packet received, executes on each packetID and
 * assigned class.
 * 
 * @author xEnt
 * 
 */
public interface PacketHandler {

	public void handlePacket(Player player, byte[] packet) throws Exception;

	public int getPacketID();

}
