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
        assertEquals("false", write(new JsonFalse()));
        assertEquals("true", write(new JsonTrue()));
    }

    @Test
    public void testMap() throws Exception {
        System.out.println("Object tests");
        var map = new JsonMap();
        map.asMap().put("int", new JsonNumber("124"));
        map.asMap().put("array", new JsonArray());
        map.asMap().put("string", new JsonString("this is a string"));
        map.asMap().put("object", new JsonMap());
        assertEquals("{\"array\":[],\"int\":124,\"object\":{},\"string\":\"this is a string\"}", write(map));
    }

    @Test
    public void testArray() throws Exception {
        System.out.println("Array tests");
        JsonArray list = new JsonArray();
        assertEquals("[]", write(list));
        list.asArray().add(new JsonNumber(123));
        assertEquals("[123]", write(list));
        list.asArray().add(new JsonString("test"));
        assertEquals("[123,\"test\"]", write(list));
        list.asArray().add(new JsonArray());
        assertEquals("[123,\"test\",[]]", write(list));
    }

    @Test
    public void testNull() throws Exception {
        System.out.println("null test");
        assertEquals("null", write(new JsonNull()));
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

    }


    private String write(String value) throws IOException {
        return write(new JsonString(value));
    }

    private String write(double value) throws IOException {
        return write(new JsonNumber(value));
    }


    private String write(JsonValue value) throws IOException {
        return value.toJsonString();
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
