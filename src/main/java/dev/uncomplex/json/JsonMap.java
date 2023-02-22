package dev.uncomplex.json;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author jthorpe
 */
public class JsonMap extends TreeMap<String,JsonValue> implements JsonValue {

    public JsonMap() {
    }

    public JsonMap(Comparator<? super String> comparator) {
        super(comparator);
    }

    public JsonMap(Map<? extends String, ? extends JsonValue> m) {
        super(m);
    }

    public JsonMap(SortedMap<String, ? extends JsonValue> m) {
        super(m);
    }
    
    @Override
    public JsonMap asMap() {
        return this;
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
