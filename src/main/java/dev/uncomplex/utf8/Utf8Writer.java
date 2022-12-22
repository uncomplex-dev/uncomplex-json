package dev.uncomplex.utf8;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author james
 */
public class Utf8Writer extends java.io.Writer {

    private final OutputStream out;
    private boolean closed = false;

    public Utf8Writer(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        for (int i = 0; i < len; ++i) {
            write(cbuf[off + i]);
        }
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        if (!closed) {
            flush();
            closed = true;
        }
    }

    /*
    Encode character as UTF-8 byte sequence.  
     */
    @Override
    public void write(int c) throws IOException {
        if (closed) {
            throw new IOException("Writer closed");
        }
        if (c <= 0x7F) {
            out.write(c);
        } else if (c <= 0x7FF) {
            out.write(0xC0 | (c >> 6));
            out.write(0x80 | (c & 0x3F));
        } else if (c <= 0xFFFF) {
            out.write(0xE0 | (c >> 12));
            out.write(0x80 | ((c >> 6) & 0x3F));
            out.write(0x80 | (c & 0x3F));
        } else if (c <= 0x10FFFF) {
            out.write(0xF0 | (c >> 18));
            out.write(0x80 | ((c >> 12) & 0x3F));
            out.write(0x80 | ((c >> 6) & 0x3F));
            out.write(0x80 | (c & 0x3F));
        } else {
            throw new UnsupportedEncodingException("Character out of range for UTF-8 encoding");
        }
    }

}
