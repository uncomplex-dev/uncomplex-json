package dev.uncomplex.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author James Thorpe
 */
public class JsonWriterTest {

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    public JsonWriterTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testBoolean() throws Exception {
        System.out.println("Boolean tests");
        assertEquals("false", write(new JsonValue(false)));
        assertEquals("true", write(new JsonValue(true)));
    }

    @Test
    public void testObject() throws Exception {
        System.out.println("Object tests");
        var map = new HashMap<String, JsonValue>();
        map.put("int", new JsonValue("124"));
        map.put("array", new JsonValue(new ArrayList<>()));
        map.put("string", new JsonValue("this is a string"));
        map.put("object", new JsonValue(new HashMap<>()));
        assertEquals("{\"string\":\"this is a string\",\"array\":[],\"int\":\"124\",\"object\":{}}", write(map));
    }

    @Test
    public void testArray() throws Exception {
        System.out.println("Array tests");
        ArrayList<JsonValue> list = new ArrayList<>();
        assertEquals("[]", write(list));
        list.add(new JsonValue(123));
        assertEquals("[123]", write(list));
        list.add(new JsonValue("test"));
        assertEquals("[123,\"test\"]", write(list));
        list.add(new JsonValue(new ArrayList<>()));
        assertEquals("[123,\"test\",[]]", write(list));
    }

    @Test
    public void testNull() throws Exception {
        System.out.println("null test");
        assertEquals("null", write(new JsonValue()));
    }

    @Test
    public void testNumbers() throws Exception {
        System.out.println("Number tests");
        assertEquals("10", write(10));
        assertEquals("123456789123", write(123456789123L));
        assertEquals(1.234, Double.parseDouble(write(1.234f)), 0.0001);
        assertEquals(1.234, Double.parseDouble(write(1.234)), 0.0001);

    }

    @Test
    public void testString() throws Exception {
        System.out.println("String tests");
        assertEquals("\"\"", write(""));
        assertEquals("\"test\"", write("test"));
        assertEquals("\"a\\\"b\"", write("a\"b"));
        assertEquals("\"\\\\\\\"\"", write("\\\""));
        assertEquals("\"\\r\\n\\t\"", write("\r\n\t"));
        assertEquals("\"\\u0000\"", write("\u0000"));
        assertEquals("\"\\u0001\"", write("\u0001"));
        assertEquals("\"a\"", write("\u0061"));
        //test UTF-8 encodings
        testUtf8CodePoints(0x7f, 0x7FF);
        testUtf8CodePoints(0x800, 0x9FF);
        testUtf8CodePoints(0xE000, 0xE0FF);
        testUtf8CodePoints(0x10000, 0x100FF);
    }

    private void testUtf8CodePoints(int start, int end) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i <= end; ++i) {
            sb.appendCodePoint(i);
        }
        String test = "\"" + sb.toString() + "\"";
        ByteArrayOutputStream out = new ByteArrayOutputStream(0x80);
        JsonWriter w = new JsonWriter(out);
        w.write(new JsonValue(sb.toString()));
        String result = out.toString(StandardCharsets.UTF_8);
        assertTrue(test.equals(result));
    }

    private String write(String value) throws IOException {
        return write(new JsonValue(value));
    }

    private String write(double value) throws IOException {
        return write(new JsonValue(value));
    }

    private String write(List<JsonValue> value) throws IOException {
        return write(new JsonValue(value));
    }

    private String write(Map<String, JsonValue> value) throws IOException {
        return write(new JsonValue(value));
    }

    private String write(JsonValue value) throws IOException {
        return value.toString();
    }


    private void writeErr(JsonValue value, String msgFragment) throws IOException {
        try {
            write(value);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(ex.getMessage().contains(msgFragment));
        }
    }

}
