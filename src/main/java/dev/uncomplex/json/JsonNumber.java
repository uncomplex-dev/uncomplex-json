package dev.uncomplex.json;

import java.math.BigDecimal;

/**
 *
 * @author jthorpe
 */
public class JsonNumber implements JsonValue {

    private final BigDecimal value;
    
    public JsonNumber(BigDecimal v) {
        value = v;
    }
    
    public JsonNumber(long v) {
        value = new BigDecimal(v);
    }

    public JsonNumber(double v) {
        value = new BigDecimal(v);
    }

    public JsonNumber(String v) {
        value = new BigDecimal(v);
    }
    
    @Override
    public boolean isNumber() {
        return true;
    }
    
    @Override
    public double asDouble() {
        return value.doubleValue();
    }
    
    @Override
    public long asLong() {
        return value.longValueExact();
    }
    
    @Override
    public BigDecimal asNumber() {
        return value;
    }
    
    @Override
    public String toString() {
        return toJsonString();
    }
    
}
