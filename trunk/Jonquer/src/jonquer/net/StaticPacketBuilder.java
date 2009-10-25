package jonquer.net;

public class StaticPacketBuilder {

    public byte[] b = new byte[1024];
    int current = 2;

    public StaticPacketBuilder(int type) {
	addShort(type);
    }

    public void setType(int type) {
	this.putInt(2, type);
    }

    public void addCStr(String str, int len) {
	byte[] dat = str.getBytes();
	System.arraycopy(dat, 0, b, current, dat.length);
	current += len;
    }

    public int getSize() {
	return getShort(0);
    }

    public byte[] getData() {
	return b;
    }

    public byte getByte(int x) {
	return (byte) (b[x] & 0xFF);
    }

    public int getShort(int x) {
	try {
	    int x1 = (b[x] & 0xFF) | ((b[x + 1] & 0xFF) << 8);
	    return x1;
	} catch (Exception e) {
	    return 0;
	}
    }

    public static int getInt(byte[] b, int x) {
	try {
	    int x1 = ((int) b[x] & 0xFF) | (((int) b[x + 1] & 0xFF) << 8) | (((int) b[x + 2] & 0xFF) << 16) | (((int) b[x + 3] & 0xFF) << 24);
	    return x1;
	} catch (Exception e) {
	    return 0;
	}
    }

    public int getInt(int x) {
	try {
	    int x1 = ((int) b[x] & 0xFF) | (((int) b[x + 1] & 0xFF) << 8) | (((int) b[x + 2] & 0xFF) << 16) | (((int) b[x + 3] & 0xFF) << 24);
	    return x1;
	} catch (Exception e) {
	    return 0;
	}
    }

    public static String getString(byte[] b, int x) {
	try {
	    byte x1[] = new byte[b[x]];
	    for (int i = 1; i < b[x] + 1; i++) {
		x1[i - 1] = b[x + i];

	    }
	    String x2 = new String(x1);
	    return x2;
	} catch (Exception e) {
	    return "";
	}
    }

    public String getString(int x) {
	try {
	    byte x1[] = new byte[b[x]];
	    for (int i = 1; i < b[x] + 1; i++) {
		x1[i - 1] = b[x + i];
	    }
	    String x2 = new String(x1);
	    return x2;
	} catch (Exception e) {
	    return "";
	}
    }

    public void setSize(int size) {
	b[0] = (byte) (size & 0xff);
	b[1] = (byte) ((size & 0xff00) >> 8);
    }

    public void addShort(int x) {
	b[current++] = (byte) (x & 0xff);
	b[current++] = (byte) ((x & 0xff00) >> 8);
    }

    public void addShort(int x[]) {
	for (int i = 0; i < x.length; i++) {
	    addShort(x[i]);
	}
    }

    public void putShort(int loc, int x) {
	b[loc++] = (byte) (x & 0xff);
	b[loc++] = (byte) ((x & 0xff00) >> 8);
    }

    public void addByte(int x) {
	b[current++] = (byte) (x);
    }

    public void addByte(byte x[]) {
	for (int i = 0; i < x.length; i++) {
	    addByte(x[i]);
	}
    }

    public void addRawBytes(short x[], int index) {
	full = true;
	for (int i = 0; i < x.length; i++) {
	    b[index] = (byte) x[i];
	    index++;
	}
    }

    boolean full = false;

    public void putByte(int loc, int x) {
	b[loc] = (byte) (x & 0xff);
    }

    public void finalizeNoSize() {
	byte x[] = new byte[current];
	System.arraycopy(b, 0, x, 0, current);
	b = new byte[x.length];
	System.arraycopy(x, 0, b, 0, x.length);
    }

    public byte[] toArray() {

	byte x[] = new byte[current];
	System.arraycopy(b, 0, x, 0, current);
	if (full)
	    return x;
	b = new byte[x.length];
	System.arraycopy(x, 0, b, 0, x.length);
	putShort(0, current);
	return b;
    }

    public void addInt(int x) {
	b[current++] = (byte) (x & 0xff);
	b[current++] = (byte) ((x & 0xff00) >> 8);
	b[current++] = (byte) ((x & 0xff0000) >> 16);
	b[current++] = (byte) ((x & 0xff000000) >> 24);
    }

    public void putInt(int loc, int x) {
	b[loc++] = (byte) (x & 0xff);
	b[loc++] = (byte) ((x & 0xff00) >> 8);
	b[loc++] = (byte) ((x & 0xff0000) >> 16);
	b[loc++] = (byte) ((x & 0xff000000) >> 24);
    }

    public void addLong(int x) {
	b[current++] = ((byte) (x & 0xff));
	b[current++] = ((byte) (x >> 8 & 0xff));
	b[current++] = (byte) (x >> 16 & 0xff);
	b[current++] = ((byte) (x >> 24 & 0xff));
    }

    public void addInt(int x[]) {
	for (int i = 0; i < x.length; i++) {
	    addInt(x[i]);
	}
    }

    public void addString(String x) {
	b[current++] = (byte) x.length();
	byte xx[] = x.getBytes();
	for (int i = 0; i < x.length(); i++) {
	    b[current + i] = xx[i];
	}
	current += x.length();
    }

    public void putString(int loc, String x) {
	int old_size = getSize();
	int old_str_size = getString(loc).length();
	int new_size = old_size - old_str_size + x.length();
	setSize(new_size);
	byte tmp[] = new byte[new_size];
	byte str[] = x.getBytes();
	int difference = old_str_size - x.length();
	boolean offset = false;
	int str_counter = 0;
	for (int i = 0; i < new_size; i++) {
	    if (i == loc) {
		tmp[i] = (byte) x.length();
		i++;
		for (int i2 = 0; i2 < x.length(); i2++) {
		    tmp[i] = str[str_counter++];
		    i++;
		}
		offset = true;
	    } else {
		if (offset) {
		    tmp[i] = b[i + difference];
		} else {
		    tmp[i] = b[i];
		}
	    }
	}
	b = tmp;
	setSize(new_size);
    }

    public long searchInt(long x) {
	for (int i = 0; i <= getSize() - 4; i += 4) {
	    if (getInt(i) == x) {
		return i;
	    }
	}
	return 0;
    }

    public int searchShort(int x) {
	for (int i = 0; i <= getSize() - 2; i += 2) {
	    if (getShort(i) == x) {
		return i;
	    }
	}
	return 0;
    }

    public int searchByte(char x) {
	for (int i = 0; i <= getSize(); i++) {
	    if (getByte(i) == x) {
		return i;
	    }
	}
	return 0;
    }
}