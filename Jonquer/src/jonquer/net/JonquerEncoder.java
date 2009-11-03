package jonquer.net;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class JonquerEncoder implements ProtocolEncoder {

    @Override
    public void encode(IoSession arg0, Object arg1, ProtocolEncoderOutput arg2) throws Exception {
	 arg2.write((ByteBuffer)arg1);
    }

    @Override
    public void dispose(IoSession arg0) throws Exception {
    }

}
