package dev.uncomplex.json;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author jthorpe
 */
public class JsonArray extends ArrayList<JsonValue> implements JsonValue {

    public JsonArray(int initialCapacity) {
        super(initialCapacity);
    }

    public JsonArray() {
    }

    public JsonArray(Collection<? extends JsonValue> c) {
        super(c);
    }
    
    @Override
    public JsonArray asArray() {
        return this;
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
