package jonquer.net;

import jonquer.game.Constants;
import jonquer.model.Packet;
import jonquer.model.Player;
import jonquer.util.Crypto;
import jonquer.util.Log;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoSession;

/**
 * the MINA incoming packet handler for the port game port stream.
 * 
 * @author xEnt
 * 
 */
public class GameConnectionHandler implements IoHandler {

	/**
	 * Thrown when an exception has been thrown by MINA
	 */
	public void exceptionCaught(IoSession session, Throwable arg1) throws Exception {
	}

	/**
	 * Called by MINA when a packet is sent from a client to server This will
	 * get the packet, decrypt it and add it to the players packet queue which
	 * will then be handled by the PacketHandler interface and scattered out to
	 * the correct classes
	 */
	public void messageReceived(IoSession sess, Object msg) {
		Player player = (Player) sess.getAttachment();
		ByteBuffer buff = (ByteBuffer) msg;
		byte[] bb = new byte[buff.remaining()];
		buff.get(bb, 0, bb.length);
		player.getIncomingPackets().add(new Packet(bb, Packet.GAME_SERVER));

	}

	public void messageSent(IoSession arg0, Object arg1) throws Exception {

	}

	/**
	 * When someone disconnects and the session closes
	 */
	public void sessionClosed(IoSession arg0) throws Exception {
		Player player = (Player) arg0.getAttachment();
		player.destroy();
	}

	/**
	 * Notification that a session has been Created.
	 */
	public void sessionCreated(IoSession arg0) throws Exception {

	}

	/**
	 * Called when a session has been idle, and nothing has been sent/received
	 * over the stream for the set idle time.
	 */
	public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {
		Player player = (Player) arg0.getAttachment();
	}

	/**
	 * When a session is Opened, attach a new Player object to the stream and
	 * set the idle time.
	 */
	public void sessionOpened(IoSession session) throws Exception {
		Constants.PEAK_PLAYER_COUNT++;
		Player p = new Player(session, 100000 + Constants.PEAK_PLAYER_COUNT);
		session.setAttachment(p);
		session.setIdleTime(IdleStatus.BOTH_IDLE, 10);
		session.setWriteTimeout(30);
		Log.debug("(GameServer) Connection Created");
	}

}
