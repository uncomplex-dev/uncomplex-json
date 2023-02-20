package dev.uncomplex.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 *
 * @author jthorpe
 */
public class JsonNumber extends BigDecimal implements JsonValue {

    public JsonNumber(char[] in, int offset, int len) {
        super(in, offset, len);
    }

    public JsonNumber(char[] in, int offset, int len, MathContext mc) {
        super(in, offset, len, mc);
    }

    public JsonNumber(char[] in) {
        super(in);
    }

    public JsonNumber(char[] in, MathContext mc) {
        super(in, mc);
    }

    public JsonNumber(String val) {
        super(val);
    }

    public JsonNumber(String val, MathContext mc) {
        super(val, mc);
    }

    public JsonNumber(double val) {
        super(val);
    }

    public JsonNumber(double val, MathContext mc) {
        super(val, mc);
    }

    public JsonNumber(BigInteger val) {
        super(val);
    }

    public JsonNumber(BigInteger val, MathContext mc) {
        super(val, mc);
    }

    public JsonNumber(BigInteger unscaledVal, int scale) {
        super(unscaledVal, scale);
    }

    public JsonNumber(BigInteger unscaledVal, int scale, MathContext mc) {
        super(unscaledVal, scale, mc);
    }

    public JsonNumber(int val) {
        super(val);
    }

    public JsonNumber(int val, MathContext mc) {
        super(val, mc);
    }

    public JsonNumber(long val) {
        super(val);
    }

    public JsonNumber(long val, MathContext mc) {
        super(val, mc);
    }
    
    @Override
    public boolean isNumber() {
        return true;
    }
    
    @Override
    public BigDecimal asNumber() {
        return this;
    }
    
}
