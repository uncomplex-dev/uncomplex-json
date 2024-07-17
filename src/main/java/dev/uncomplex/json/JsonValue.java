package dev.uncomplex.json;

import dev.uncomplex.utf8.Utf8Writer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jthorpe
 */
public interface JsonValue {
    
    default boolean isArray() {
        return false;
    }

    default boolean isBoolean() {
        return false;
    }
    
    default boolean isNull() {
        return false;
    }

    default boolean isNumber() {
        return false;
    }

    default boolean isMap() {
        return false;
    }

    default boolean isString() {
        return false;
    }


    default List<JsonValue> asArray() {
        throw new ClassCastException("value is not an array");
    }

    default boolean asBoolean() {
        throw new ClassCastException("value is not boolean");
    }

    default double asDouble() {
        throw new ClassCastException("value is not a number");
    }

    default long asLong() {
        throw new ClassCastException("value is not a number");
    }
    
    
    default BigDecimal asNumber() {
        throw new ClassCastException("value is not a number");
    }
   
    default Map<String,JsonValue> asMap() {
        throw new ClassCastException("value is not a map");
    }

    default String asString() {
        throw new ClassCastException("value is not a string");
    }
    
    default boolean contains(String key) {
        return asMap().containsKey(key);
    }
    
    default JsonValue get(String key) {
        return asMap().getOrDefault(key, new JsonNull());
    }
    
    static JsonMap put(String key, JsonValue val) {
        return new JsonMap().put(key, val);
    }
    
    static JsonMap put(String key, Collection<? extends JsonValue> val) {
        return new JsonMap().put(key, new JsonArray(val));
    }

    static JsonMap put(String key, boolean val) {
        return new JsonMap().put(key, val ? new JsonTrue() : new JsonFalse());
    }

    static JsonMap put(String key, long val) {
        return new JsonMap().put(key, new JsonNumber(val));
    }

    static JsonMap put(String key, double val) {
        return new JsonMap().put(key, new JsonNumber(val));
    }

    static JsonMap put(String key, BigDecimal val) {
        return new JsonMap().put(key, new JsonNumber(val));
    }
    
    static JsonMap put(String key, String val) {
        return new JsonMap().put(key, new JsonString(val));
    }
        
    
    default byte[] toBytes() {
        try {
            var bytes = new ByteArrayOutputStream();
            new JsonWriter(new Utf8Writer(bytes)).write(this);
            return bytes.toByteArray();
        } catch (IOException e) {
            return new byte[0]; // this will not happen
        }
    }
    
    default String toJsonString() {
        try {
            var out = new StringWriter();
            new JsonWriter(out).write(this);
            return out.toString();
        } catch (IOException e) {
            return ""; // this will not happen
        }
    }
    
    default void toWriter(Writer w) throws IOException {
        try (var jw = new JsonWriter(new BufferedWriter(w))) {
            jw.write(this);
        }
    }
    
    default void toStream(OutputStream out) throws IOException {
        try (var jw = new JsonWriter(new BufferedWriter(new OutputStreamWriter(out)))) {
            jw.write(this);
        }
    }
    
    static JsonValue fromBytes(byte[] b) throws IOException, ParseException {
        return new JsonReader(new InputStreamReader( new ByteArrayInputStream(b))).read();
    }
    
    static JsonValue fromString(String s) throws IOException, ParseException {
        return new JsonReader(s).read();
    }
    
    static JsonValue fromReader(Reader r) throws IOException, ParseException {
        return new JsonReader(new BufferedReader(r)).read();
    }
    
    static JsonValue fromStream(InputStream in) throws IOException, ParseException {
        return new JsonReader(new BufferedReader(new InputStreamReader(in))).read();
    }
    
}
