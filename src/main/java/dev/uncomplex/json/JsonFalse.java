package dev.uncomplex.json;

/**
 *
 * @author jthorpe
 */
public class JsonFalse implements JsonValue {
    @Override
    public boolean asBoolean() {
        return false;
    }
    
    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public String toString() {
        return "false";
    }
    
}
