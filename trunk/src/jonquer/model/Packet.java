package jonquer.model;

public class Packet {

    public Packet(byte[] data, boolean encrypt) {
	this.encrypt = encrypt;
	this.data = data;
    }

    public Packet(byte[] data, int type) {
	this.data = data;
    }

    public Packet(byte[] data) {
	this.data = data;
    }

    private int type = 0;
    private boolean encrypt = true;
    private byte[] data;
    public static final int GAME_SERVER = 1;
    public static final int AUTH_SERVER = 0;

    public int getType() {
	return type;
    }

    public byte[] getData() {
	return this.data;
    }

    public boolean needsEncryption() {
	return this.encrypt;
    }
}
