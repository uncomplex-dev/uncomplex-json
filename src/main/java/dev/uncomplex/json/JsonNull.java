package dev.uncomplex.json;

import java.math.BigDecimal;

/**
 *
 * @author jthorpe
 */
public class JsonNull implements JsonValue {

    @Override
    public double asDouble() {
        return 0.0;
    }

    @Override
    public long asLong() {
        return 0L;
    }

    @Override
    public BigDecimal asNumber() {
        return BigDecimal.ZERO;
    }

    @Override
    public String asString() {
        return "";
    }

    @Override
    public boolean isNull() {
        return true;
    }
    
    @Override
    public String toString() {
        return "null";
    }
    
    @Override
    public String toJsonString() {
        return "null";
    }    

}
