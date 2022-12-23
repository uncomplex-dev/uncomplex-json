package dev.uncomplex.json;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author James Thorpe
 */
public class JsonWriter implements Closeable {

    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final Logger LOG = Logger.getLogger(JsonWriter.class.getName());

    private final Writer out;

    public JsonWriter(Writer out) {
        this.out = out;
    }

    @Override
    public void close() throws IOException {
        out.close();
    }

    /**
     * Write value to the OutputStream.
     * The value is always written using UTF-8 encoding.  It is not possible to change this.
     *
     * @param value
     * @throws IOException if there is an error writing to the OutputStream
     * @throws NullPointerException is value is null
     */
    public void write(JsonValue value) throws IOException {
        writeValue(value);
    }


    protected void writeByte(int b) throws IOException {
        out.write(b);
    }


 
    private void writeChars(String s) throws IOException {
        out.write(s);
    }

    private void writeArray(List<JsonValue> list) throws IOException {
        out.write('[');
        String separator = "";
        for (var value : list) {
            writeChars(separator);
            writeValue(value);
            separator = ",";
        }
        out.write(']');
    }

    private void writeObject(Map<String, JsonValue> map) throws IOException {
        String separator = "";
        out.write('{');
        for (var e : map.entrySet()) {
            writeChars(separator);
            writeString(e.getKey());
            out.write(':');
            writeValue(e.getValue());
            separator = ",";
        }
        out.write('}');
    }

    private void writeNumber(BigDecimal number) throws IOException {
        writeChars(number.stripTrailingZeros().toPlainString());
    }

    private void writeString(String s) throws IOException {
        out.write('"');
        for (int c : (Iterable<Integer>) () -> s.codePoints().iterator()) {
            switch (c) {
                case '"' -> writeChars("\\\"");
                case '\\' -> writeChars("\\\\");
                case '\n' -> writeChars("\\n");
                case '\r' -> writeChars("\\r");
                case '\t' -> writeChars("\\t");
                case '\b' -> writeChars( "\\b");
                case '\f' -> writeChars( "\\f");
                default -> {
                    if (c < ' ') {
                        writeChars("\\u00");
                        out.write(HEX_CHARS[(c >> 4) & 0xF]);
                        out.write(HEX_CHARS[c & 0xF]);
                    } else {
                        out.write(c);
                    }
                }
            }
        }
        out.write('"');
    }

    private void writeValue(JsonValue value) throws IOException {
        if (value.isNull()) {
            writeChars("null");
        } else if (value.isObject()) {
            writeObject(value.asObject());
        } else if (value.isArray()) {
            writeArray(value.asArray());
        } else if (value.isString()) {
            writeString(value.asString());
        } else if (value.isNumber()) {
            writeNumber(value.asDecimal());
        } else if (value.isBoolean()) {
            writeChars(value.asBoolean() ? "true" : "false");
        }
    }

}
