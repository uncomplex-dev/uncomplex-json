package dev.uncomplex.utf8;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author james
 */
public class Utf8Reader extends java.io.Reader {

    /*
        The length of the utf8 octet sequence 
        based on the first octet in the sequence.  A length of zero
        indicates an illegal encoding.
     */
    private static final byte[] UTF8_LENGTH = {
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
        2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
        4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };

    /*
        Adjustment values used to 'remove' the tag bits in each utf8
        sequence.  These are constant for any sequence of a given length
        and can be removed with a single subtraction.
     */
    private static final int[] UTF8_TAG = {
        0x00000000,
        0x00000000,
        0x00003080,
        0x000E2080,
        0x03C82080
    };

    private final InputStream in;

    public Utf8Reader(InputStream in) {
        this.in = in;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        for(int i = 0; i < len; ++i) {
            int c = read();
            if (c == -1) return i;
            cbuf[off + i] = (char)read();
        }
        return len;
    }

    @Override
    public void close() throws IOException {
        // null
    }

    @Override
    public int read() throws IOException {
        int utf8 = in.read();
        if (utf8 != -1) {
            int len = UTF8_LENGTH[utf8];
            for (int i = 1; i < len; ++i) {
                int c = in.read();
                if (c == -1) {
                    throw new IOException("Invalid UTF8 encoding in input");
                }
                utf8 = (utf8 << 6) + c;
            }
            utf8 -= UTF8_TAG[len];
        }
        return utf8;
    }

}
