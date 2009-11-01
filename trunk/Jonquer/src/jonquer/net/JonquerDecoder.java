package jonquer.net;

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

    public void dispose(IoSession session) throws Exception {
	super.dispose(session);
    }

    protected boolean doDecode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) {
	try {
	    //if (in.remaining() >= 4) {
	    Player p = (Player)session.getAttachment(); 
	    if(p.crypt.lastLength <= in.remaining() && p.crypt.lastLength != 0) {
		p.crypt.lastLength = 0;
		p.crypt.decrypt(in);
		byte[] payload = new byte[p.crypt.lastLength];
		in.get(payload);
		out.write(ByteBuffer.wrap(payload));
		return true;
	    }
	    p.crypt.decrypt(in);
	    int length = (in.get(1) << 8) | (in.get(0) & 0xff);
	    System.out.println("MadeLength: " + length + " BB: " + in.remaining());
	    if (length <= in.remaining()) {
		if (length < 0) {
		    Log.log("Negative array length! id=" + in.getUnsigned() + ",len=" + length);
		    //p.crypt.in--;
		    System.out.println("Rewinding1");
		    //in.rewind();
		    //in.flip();
		    return true;
		}
		write(p, in, out);
		return true;
	    } else {
		p.crypt.in--;
		System.out.println("Rewinding2");
		p.crypt.lastLength = length;
		in.rewind();
		return false;
	    }

	} catch (Exception e) {
	    e.printStackTrace();
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
