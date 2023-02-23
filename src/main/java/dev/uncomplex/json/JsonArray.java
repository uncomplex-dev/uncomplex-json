package dev.uncomplex.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author jthorpe
 */
public class JsonArray implements JsonValue {
    private final ArrayList<JsonValue> value;

    public JsonArray(int initialCapacity) {
        value = new ArrayList<>(initialCapacity);
    }

    public JsonArray() {
        value = new ArrayList<>();
    }

    public JsonArray(Collection<? extends JsonValue> c) {
        value = new ArrayList<>(c);
    }
    
    
    @Override
    public List<JsonValue> asArray() {
        return value;
    }
    
    @Override
    public boolean isArray() {
        return true;
    }
    
    @Override
    public String toString() {
        return toJsonString();
    }
}
