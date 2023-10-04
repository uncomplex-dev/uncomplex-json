/*
	Lambda Limited
 */
package dev.uncomplex.json;

import java.io.IOException;
import java.text.ParseException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


/**
 *
 * @author James Thorpe
 * 
 * 
 */
public class JsonReaderTest {

    public JsonReaderTest() {
    }

    /**
     * Test of close method, of class Reader.
     */
    @Test
    public void testConstants() throws Exception {
        System.out.println("Constant Tests");
        assertEquals(true, read("true").asBoolean());
        assertEquals(false, read("false").asBoolean());
        assertTrue(read("null").isNull());
        readErr("crap", "invalid value");
    }

    @Test
    public void testNumbers() throws Exception {
        System.out.println("Number Tests");
        assertEquals(0, read("0").asNumber().intValueExact(), 0);
        assertEquals(123, read("123").asNumber().longValueExact(), 0);
        assertEquals(1.2, read("1.2").asNumber().doubleValue(), 0.0001);
        assertEquals(-1.23e-97, read("-1.23e-97").asNumber().doubleValue(), 0.0001);
        assertEquals(+1.23E+97, read("+1.23E+97").asNumber().doubleValue(), 0.0001);
    }


    @Test
    public void testStrings() throws Exception {
        System.out.println("String Tests");
        assertEquals("", read("\"\"").asString());
        assertEquals("test", read("\"test\"").asString());
        assertEquals("a\"b", read("\"a\\\"b\"").asString());
        assertEquals("\\\"", read("\"\\\\\\\"\"").asString());
        assertEquals("\r\n\t\f\b", read("\"\\r\\n\\t\\f\\b\"").asString());
        assertEquals("\u0000", read("\"\\u0000\"").asString());
        assertEquals("\u0001", read("\"\\u0001\"").asString());
        assertEquals("\u0ABC", read("\"\\u0ABC\"").asString());
        assertEquals("\u0abc", read("\"\\u0abc\"").asString());
    }

    @Test
    public void testList() throws Exception {
        System.out.println("List Array");
        assertEquals("[]", readwrite("[]"));
        assertEquals("[]", readwrite(" [ ] "));
        assertEquals("[1,2,3,\"test\"]", readwrite("[1, 2 ,3, \"test\"]"));
    }

    @Test
    public void testMap() throws Exception {
        System.out.println("Map Tests");
        assertEquals("{}", readwrite("{}"));
        assertEquals("{}", readwrite(" { } "));
        assertEquals("{\"a\":6}", readwrite("{\"a\": 6}"));
        assertEquals("{\"a\":6,\"b\":false,\"c\":\"string\"}", readwrite("{\"a\":6, \"b\":false, \"c\":\"string\" }"));
        readErr("{a:9}", "'\"' expected");
    }

//    {"a":6, "b":false, "c":"string" }

    private JsonValue read(String s) throws IOException, ParseException {
        return JsonValue.fromString(s);
    }

    private void readErr(String s, String msgFragment) throws IOException {
        try {
            read(s);
            fail();
        } catch (RuntimeException | ParseException ex) {
            assertTrue(ex.getMessage().contains(msgFragment),
                    String.format("expected error message containing '%s' but got '%s'\n", msgFragment, ex.getMessage()));
        }
    }

    // read the data and then write it back out again
    // this requires the writer tests to have passed
    private String readwrite(String s) throws IOException, ParseException {
        var value = read(s);
        return write(value);
    }


    private String write(JsonValue value) throws IOException {
        return value.toJsonString();
    }


}
