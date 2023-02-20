
package dev.uncomplex.json;

/**
 *
 * @author jthorpe
 */
public class JsonNull implements JsonValue {
    @Override
    public boolean isNull() {
        return true;
    }
    
    @Override
    public String toString() {
        return "null";
    }

}
