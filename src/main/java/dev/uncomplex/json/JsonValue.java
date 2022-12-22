package dev.uncomplex.json;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonValue {

    private final Object value;

    public static JsonValue array() {
        return new JsonValue(new ArrayList<>());
    }

    public static JsonValue object() {
        return new JsonValue(new HashMap<>());
    }

    public JsonValue() {
        this.value = null;
    }

    public JsonValue(Map<String, JsonValue> value) {
        this.value = value;
    }

    public JsonValue(List<JsonValue> value) {
        this.value = value;
    }
    
    public JsonValue(BigDecimal value) {
        this.value = value;
    }

    public JsonValue(double value) {
        this.value = BigDecimal.valueOf(value);
    }

    public JsonValue(int value) {
        this.value = BigDecimal.valueOf(value);
    }

    public JsonValue(long value) {
        this.value = BigDecimal.valueOf(value);
    }

    public JsonValue(boolean value) {
        this.value = value;
    }

    public JsonValue(String value) {
        this.value = value;
    }
    

    public List<JsonValue> asArray() {
        return asType(List.class, "value is not an array");
    }

    public boolean asBoolean() {
        return asType(Boolean.class, "value is not boolean");
    }
    
    public BigDecimal asDecimal() {
        return asType(BigDecimal.class, "value is not a double number");
    }

    public double asDouble() {
        return asType(BigDecimal.class, "value is not a double").doubleValue();
    }

    public double asFloat() {
        return asType(BigDecimal.class, "value is not a float").floatValue();
    }
    
    public int asInt() {
        return asType(BigDecimal.class, "value is not a integer").intValueExact();
    }

    public long asLong() {
        return asType(BigDecimal.class, "value is not a long integer").longValueExact();
    }


    public Map<String, JsonValue> asObject() {
        return asType(Map.class, "value is not an object");
    }

    public String asString() {
        return asType(String.class, "value is not a string");
    }

    private <T> T asType(Class<T> k, String msg) {
        if (k.isInstance(value)) return k.cast(value);
        throw new ClassCastException(msg);
    }

    public JsonValue get(String key) {
        return asObject().get(key);
    }

    public JsonValue getOrDefault(String key, List<JsonValue> array) {
        return asObject().computeIfAbsent(key, k -> new JsonValue(array));
    }

    public JsonValue getOrDefault(String key, boolean b) {
        return asObject().computeIfAbsent(key, k -> new JsonValue(b));
    }

    public JsonValue getOrDefault(String key, int i) {
        return asObject().computeIfAbsent(key, k -> new JsonValue(i));
    }

    public JsonValue getOrDefault(String key, long l) {
        return asObject().computeIfAbsent(key, k -> new JsonValue(l));
    }

    public JsonValue getOrDefault(String key, double d) {
        return asObject().computeIfAbsent(key, k -> new JsonValue(d));
    }

    public JsonValue getOrDefault(String key, Map<String, JsonValue> m) {
        return asObject().computeIfAbsent(key, k -> new JsonValue(m));
    }

    public JsonValue getOrDefault(String key, String s) {
        return asObject().computeIfAbsent(key, k -> new JsonValue(s));
    }

    public boolean isArray() {
        return (value != null && value instanceof List);
    }

    public boolean isBoolean() {
        return (value != null && value instanceof Boolean);
    }

    public boolean isNull() {
        return value == null;
    }

    public boolean isNumber() {
        return (value != null && value instanceof BigDecimal);
    }

    public boolean isObject() {
        return (value != null && value instanceof Map);
    }

    public boolean isString() {
        return (value != null && value instanceof String);
    }

    public JsonValue put(String key, JsonValue value) {
        var map = asObject();
        map.put(key, value);
        return this;
    }

    public JsonValue put(String key, boolean b) {
        asObject().put(key, new JsonValue(b));
        return this;
    }

    public JsonValue put(String key, double d) {
        asObject().put(key, new JsonValue(d));
        return this;
    }

    public JsonValue put(String key, int i) {
        asObject().put(key, new JsonValue(i));
        return this;
    }

    public JsonValue put(String key, long l) {
        asObject().put(key, new JsonValue(l));
        return this;
    }

    public JsonValue put(String key, String value) {
        asObject().put(key, new JsonValue(value));
        return this;
    }

    public JsonValue put(String key, List<JsonValue> value) {
        asObject().put(key, new JsonValue(value));
        return this;
    }

    public JsonValue put(String key, Map<String, JsonValue> value) {
        asObject().put(key, new JsonValue(value));
        return this;
    }

    @Override
    public String toString() {
        try {
            var out = new StringWriter();
            var w = new JsonWriter(out);
            w.write(this);
            return out.toString();
        } catch (IOException e) {
            return super.toString();
        }
    }

    public static JsonValue fromString(String s) throws IOException, ParseException {
        return new JsonReader(s).read();
    }
}
