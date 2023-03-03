package dev.uncomplex.json;

/**
 *
 * @author jthorpe
 */
public class JsonString implements JsonValue {
    private final String value;
    
    public JsonString(String v) {
        value = v;
    }
    
    @Override
    public String asString() {
        return value;
    }
    
    @Override
    public boolean isString() {
        return true;
    }
    
    @Override
    public String toString() {
        return value;
    }    

}
