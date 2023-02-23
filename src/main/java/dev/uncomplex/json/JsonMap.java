package dev.uncomplex.json;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author jthorpe
 */
public class JsonMap implements JsonValue {
    
    private final TreeMap<String,JsonValue> value;
    
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
    public Map<String,JsonValue> asMap() {
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
