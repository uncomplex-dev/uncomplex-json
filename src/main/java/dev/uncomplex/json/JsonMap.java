package dev.uncomplex.json;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author jthorpe
 */
public class JsonMap implements JsonValue {

    private final TreeMap<String, JsonValue> value;

    public JsonMap put(String key, JsonValue val) {
        asMap().put(key, val);
        return this;
    }

    public JsonMap put(String key, Collection<? extends JsonValue> val) {
        return put(key, new JsonArray(val));
    }

    public JsonMap put(String key, boolean val) {
        return put(key, val ? new JsonTrue() : new JsonFalse());
    }

    public JsonMap put(String key, long val) {
        return put(key, new JsonNumber(val));
    }

    public JsonMap put(String key, double val) {
        return put(key, new JsonNumber(val));
    }

    public JsonMap put(String key, BigDecimal val) {
        return put(key, new JsonNumber(val));
    }
    
    public JsonMap put(String key, String val) {
        return put(key, new JsonString(val));
    }
    
    public JsonMap() {
        value = new TreeMap<>();
    }

    public JsonMap(Comparator<? super String> comparator) {
        value = new TreeMap<>(comparator);
    }

    public JsonMap(Map<? extends String, ? extends JsonValue> m) {
        value = new TreeMap(m);
    }

    public JsonMap(SortedMap<String, ? extends JsonValue> m) {
        value = new TreeMap(m);
    }

    @Override
    public Map<String, JsonValue> asMap() {
        return value;
    }

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public String toString() {
        return toJsonString();
    }

}
