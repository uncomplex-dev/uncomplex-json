package dev.uncomplex.json;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
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
    
    
    default String toJsonString() {
        try {
            var out = new StringWriter();
            var w = new JsonWriter(out);
            w.write(this);
            return out.toString();
        } catch (IOException e) {
            return ""; // this will not happen
        }
    }
    
    static JsonValue fromString(String s) throws IOException, ParseException {
        return new JsonReader(s).read();
    }
    
}
