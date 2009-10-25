package jonquer.util;

import java.nio.ByteOrder;

import java.nio.ByteBuffer;

public class Crypto {

    private static final byte[] cryptKey1;
    private static final byte[] cryptKey2;

    static {
	cryptKey1 = new byte[0x100];
	cryptKey2 = new byte[0x100];
	byte iKey1 = (byte) 0x9d;
	byte iKey2 = 0x62;
	for (int i = 0; i < 0x100; i++) {
	    cryptKey1[i] = iKey1;
	    cryptKey2[i] = iKey2;
	    iKey1 = (byte) ((0x0f + (byte) (iKey1 * 0xfa)) * iKey1 + 0x13);
	    iKey2 = (byte) ((0x79 - (byte) (iKey2 * 0x5c)) * iKey2 + 0x6d);
	}
    }

    private byte[] cryptKey3;
    private byte[] cryptKey4;
    private boolean alternate;
    private int in;
    private int out;

    public void generateKeys(int token, int accountId) {
	cryptKey3 = new byte[0x100];
	cryptKey4 = new byte[0x100];
	int tmpKey1 = ((accountId + token) ^ 0x4321) ^ accountId;
	int tmpKey2 = tmpKey1 * tmpKey1;
	byte[] tmp1 = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(tmpKey1).array();
	byte[] tmp2 = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(tmpKey2).array();
	for (int i = 0; i < 0x100; i++) {
	    cryptKey3[i] = (byte) (cryptKey1[i] ^ tmp1[i % 4]);
	    cryptKey4[i] = (byte) (cryptKey2[i] ^ tmp2[i % 4]);
	}
	out = 0;
	alternate = true;
    }

    public void encrypt(byte[] packet) {
	for (int i = 0; i < packet.length; i++) {
	    packet[i] = (byte) ((packet[i] & 0xff) ^ 0xab);
	    packet[i] = (byte) (((packet[i] & 0xff) << 4) | ((packet[i] & 0xff) >> 4));
	    packet[i] = (byte) (cryptKey2[out >> 8] ^ (packet[i] & 0xff));
	    packet[i] = (byte) (cryptKey1[out & 0xff] ^ (packet[i] & 0xff));
	    out++;
	}
    }

    public void decrypt(byte[] packet) {
	for (int i = 0; i < packet.length; i++) {
	    packet[i] = (byte) ((packet[i] & 0xff) ^ 0xab);
	    packet[i] = (byte) (((packet[i] & 0xff) << 4) | ((packet[i] & 0xff) >> 4));
	    if (alternate) {
		packet[i] = (byte) (cryptKey4[in >> 8] ^ (packet[i] & 0xff));
		packet[i] = (byte) (cryptKey3[in & 0xff] ^ (packet[i] & 0xff));
	    } else {
		packet[i] = (byte) (cryptKey2[in >> 8] ^ (packet[i] & 0xff));
		packet[i] = (byte) (cryptKey1[in & 0xff] ^ (packet[i] & 0xff));
	    }
	    this.in++;
	}
    }
}
