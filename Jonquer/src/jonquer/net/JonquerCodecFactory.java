package jonquer.net;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class JonquerCodecFactory implements ProtocolCodecFactory {
    

	private final JonquerEncoder m_encoder;
	private final JonquerDecoder m_decoder;
	
	/**
	 * Default constructor.
	 */
	public JonquerCodecFactory() {
		m_encoder = new JonquerEncoder();
		m_decoder = new JonquerDecoder();
	}
	
	/**
	 * Returns the decoder
	 */
	public ProtocolDecoder getDecoder() throws Exception {
		return m_decoder;
	}

	/**
	 * Returns the encoder
	 */
	public ProtocolEncoder getEncoder() throws Exception {
		return m_encoder;
	}



}
