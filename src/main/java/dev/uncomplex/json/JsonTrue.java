package dev.uncomplex.json;

/**
 *
 * @author jthorpe
 */
public class JsonTrue implements JsonValue {
    @Override
    public boolean asBoolean() {
        return true;
    }
    
    @Override
    public boolean isBoolean() {
        return true;
    }
    
    
    @Override
    public String toString() {
        return "true";
    }
    
        
    @Override
    public String toJsonString() {
        return "true";
    }
}
