
package dev.uncomplex.utf8;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author James Thorpe
 */
public class Utf8ReaderWriterTest {

    public Utf8ReaderWriterTest() {
    }

    @Test
    public void testReader() throws IOException {
        testUtf8CodePoints(0x0, 0x7F);          // 1 bytes
        testUtf8CodePoints(0x80, 0x7FF);        // 2 bytes
        testUtf8CodePoints(0x800, 0xFFFF);      // 3 bytes
        testUtf8CodePoints(0x10000, 0x100FF);   // 4 bytes (sample)
    }

    @Test
    public void testClose()  throws IOException {
        // write after close should throw an IOException
        ByteArrayOutputStream out = new ByteArrayOutputStream(10);
        Utf8Writer w = new Utf8Writer(out);
        w.write("test");
        w.close();
        w.close(); // should do nothing
        assertThrows(IOException.class, () -> { w.write( "test"); }); // should throw.
    }
    
    private void testUtf8CodePoints(int start, int end) throws IOException {
        StringBuilder original = new StringBuilder();
        for (int i = start; i <= end; ++i) {
            original.appendCodePoint(i);
        }
        // write code points to array
        ByteArrayOutputStream out = new ByteArrayOutputStream((end - start)*4);
        Utf8Writer w = new Utf8Writer(out);
        w.write(original.toString());
        
        // read them back again
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        Utf8Reader r = new Utf8Reader(in);
        var copy = new StringBuilder();
        for (int c = r.read(); c != -1; c = r.read()) {
            copy.appendCodePoint(c);
        }
        // compare
        int l = Math.min(copy.length(), original.length());
        for (int i = 0; i < l; ++i) {
            if (copy.charAt(i) != original.charAt(i)) {
                fail(String.format("codepoint mismatch at offset %d", i));
            }
        }
        if (copy.length() != original.length()) {
            fail("original and copy have different lengths");
        }
        assertTrue(copy.toString().equals(original.toString()));
    }
}
