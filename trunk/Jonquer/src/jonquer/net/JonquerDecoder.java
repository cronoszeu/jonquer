package jonquer.net;

import java.util.Arrays;
import jonquer.model.Packet;
import jonquer.model.Player;
import jonquer.util.Log;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * A Custom Decoder implemented to form incoming TCP packets better.
 * @author xEnt
 *
 */
public class JonquerDecoder extends CumulativeProtocolDecoder {
    private byte[] buffer;

    public JonquerDecoder() {
        buffer = new byte[0];
    }

    public void dispose(IoSession session) throws Exception {
        super.dispose(session);
    }
    
    protected boolean doDecode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) {
        Player player = (Player) session.getAttachment();
        player.crypt.decrypt(in);
        int offset = buffer.length;
        buffer = Arrays.copyOf(buffer, buffer.length + in.limit());
        byte[] buf = new byte[in.limit()];
        in.get(buf);
        System.arraycopy(buf, 0, buffer, offset, buf.length);
        if (buffer.length > 2) {
            int length = (buffer[1] << 8) | (buffer[0] & 0xff);
            while (length <= buffer.length) {
                write(player, ByteBuffer.wrap(Arrays.copyOfRange(buffer, 0, length)), out);
                buffer = Arrays.copyOfRange(buffer, length, buffer.length);
                if (buffer.length > 2) {
                    length = (buffer[1] << 8) | (buffer[0] & 0xff);
                } else {
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public void write(Player p, ByteBuffer in, ProtocolDecoderOutput out) {
        byte[] payload = new byte[in.remaining()];
        in.get(payload);
        in.flip();
        out.write(ByteBuffer.wrap(payload));
    }
}
