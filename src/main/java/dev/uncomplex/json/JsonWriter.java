package dev.uncomplex.json;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author James Thorpe
 */
public class JsonWriter implements Closeable {

    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final Logger LOG = Logger.getLogger(JsonWriter.class.getName());

    private final OutputStream out;

    public JsonWriter(OutputStream out) {
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


    /*
    Encode character as UTF-8 byte sequence.  
     */
    private void writeChar(int c) throws IOException {
        if (c <= 0x7F) {
            writeByte(c);
        } else if (c <= 0x7FF) {
            writeByte(0xC0 | (c >> 6));
            writeByte(0x80 | (c & 0x3F));
        } else if (c <= 0xFFFF) {
            writeByte(0xE0 | (c >> 12));
            writeByte(0x80 | ((c >> 6) & 0x3F));
            writeByte(0x80 | (c & 0x3F));
        } else if (c <= 0x10FFFF) {
            writeByte(0xF0 | (c >> 18));
            writeByte(0x80 | ((c >> 12) & 0x3F));
            writeByte(0x80 | ((c >> 6) & 0x3F));
            writeByte(0x80 | (c & 0x3F));
        } else {
            throw new UnsupportedEncodingException("Character out of range for UTF-8 encoding");
        }
    }

    private void writeChars(String s) throws IOException {
        for (int c : (Iterable<Integer>) () -> s.codePoints().iterator()) {
            writeChar(c);
        }
    }

    private void writeArray(List<JsonValue> list) throws IOException {
        writeChar('[');
        String separator = "";
        for (var value : list) {
            writeChars(separator);
            writeValue(value);
            separator = ",";
        }
        writeChar(']');
    }

    private void writeObject(Map<String, JsonValue> map) throws IOException {
        String separator = "";
        writeChar('{');
        for (var e : map.entrySet()) {
            writeChars(separator);
            writeString(e.getKey());
            writeChar(':');
            writeValue(e.getValue());
            separator = ",";
        }
        writeChar('}');
    }

    private void writeNumber(double number) throws IOException {
        if (number % 1L == 0) {
            writeChars(Long.toString((long)number));
        } else {
            writeChars(Double.toString(number));
        }
    }

    private void writeString(String s) throws IOException {
        writeChar('"');
        for (int c : (Iterable<Integer>) () -> s.codePoints().iterator()) {
            switch (c) {
                case '"':
                    writeChars("\\\"");
                    break;
                case '\\':
                    writeChars("\\\\");
                    break;
                case '\n':
                    writeChars("\\n");
                    break;
                case '\r':
                    writeChars("\\r");
                    break;
                case '\t':
                    writeChars("\\t");
                    break;
                case '\b':
                    writeChars( "\\b");
                    break;
                case '\f':
                    writeChars( "\\f");
                    break;
                default:
                    if (c < ' ') {
                        writeChars("\\u00");
                        writeChar(HEX_CHARS[(c >> 4) & 0xF]);
                        writeChar(HEX_CHARS[c & 0xF]);
                    } else {
                        writeChar(c);
                    }
            }
        }
        writeChar('"');
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
            writeNumber(value.asNumber());
        } else if (value.isBoolean()) {
            writeChars(value.asBoolean() ? "true" : "false");
        }
    }

}
