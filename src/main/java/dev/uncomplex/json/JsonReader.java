package dev.uncomplex.json;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import java.util.List;

/**
 * @author James Thorpe
 * <p>
 * Reader for JSON format
 * https://www.ecma-international.org/publications-and-standards/standards/ecma-404/
 * <p>
 * For pedagogical reasons the parser is implemented as a simple top-down parser
 * which closely mirrors the grammar. It is more than 'fast
 * enough'(tm) for high volume use and the loss of simplicity caused by
 * optimisation is not worth it in my opinion.
 * <p>
 * Refer to https://www.json.org/json-en.html for grammer in McKeeman form.
 * <p>
 */
public class JsonReader {

    private int c;
    private final Reader r;
    private int pos = 0;

    public JsonReader(Reader r) {
        this.r = r;
    }

    public JsonReader(String input) {
        this(new StringReader(input));
    }

    public JsonValue read() throws IOException, ParseException {
        readChar();
        skipWs();
        if (c == -1) {
            return null;
        }
        JsonValue value = readValue();
        skipWs();
        if (c != -1) {
            throw error("end of input expected");
        }
        return value;
    }

    private boolean consume(int ch) throws IOException {
        if (ch == c) {
            readChar();
            return true;
        }
        return false;
    }

    private void consumeOrError(int ch) throws IOException, ParseException {
        if (ch == c) {
            readChar();
        } else {
            throw error("'%c' expected but '%c' found", ch, c);
        }
    }

    private ParseException error(String msg, Object... params) {
        String m = String.format("Reader Error [%d]: %s", pos, msg);
        return new ParseException(String.format(m, params), pos);
    }

    /* 
    separator characters that can occur after values in arrays and maps
    and after identifiers in objects
     */
    private boolean isSeparator(int ch) {
        return ch == ':' || ch == ',' || ch == '{' || ch == '}' || ch == '[' || ch == ']' || ch == -1;
    }

    private boolean isWs(int ch) {
        return ch == 0x20 || ch == 0x09 || ch == 0x0a || ch == 0x0d;
    }

    /*
    Read and decode UTF-8 encoded character from input byte stream
     */
    private void readChar() throws IOException {
        c = r.read();
        ++pos;
    }

    private int readHexDigit() throws IOException, ParseException {
        int ch = c;
        if (ch >= '0' && ch <= '9') {
            readChar();
            return ch - '0';
        }
        if (ch >= 'A' && ch <= 'F') {
            readChar();
            return ch + 10 - 'A';
        }
        if (ch >= 'a' && ch <= 'f') {
            readChar();
            return ch + 10 - 'a';
        }
        throw error("invalid hex digit '%c'", ch);
    }

    private JsonArray readArray() throws IOException, ParseException {
        readChar(); // skip '['
        var list = new JsonArray();
        skipWs();
        if (!consume(']')) {
            readArrayElements(list.asArray());
            consumeOrError(']');
        }
        return list;
    }

    private void readArrayElement(List<JsonValue> list) throws IOException, ParseException {
        skipWs();
        JsonValue value = readValue();
        skipWs();
        list.add(value);
    }

    private void readArrayElements(List<JsonValue> list) throws IOException, ParseException {
        readArrayElement(list);
        while (consume(',')) {
            readArrayElement(list);
        }
    }

    private JsonMap readMap() throws IOException, ParseException {
        readChar(); // skip '{'
        var map = new JsonMap();
        skipWs();
        if (!consume('}')) {
            readMembers(map);
            consumeOrError('}');
        }
        return map;
    }

    private void readMember(JsonMap map) throws IOException, ParseException {
        skipWs();
        String key = readString();
        skipWs();
        consumeOrError(':');
        skipWs();
        JsonValue value = readValue();
        skipWs();
        map.asMap().put(key, value);
    }

    private void readMembers(JsonMap map) throws IOException, ParseException {
        readMember(map);
        while (consume(',')) {
            readMember(map);
        }
    }

    /*
    readNumber() since the standard Java BigDecimal has the same format
    as the lambda number we do not have to parse
     */
    private JsonNumber readNumber() throws IOException, ParseException {
        try {
            return new JsonNumber(readToken());
        } catch (NumberFormatException ex) {
            throw error("invalid number");
        }
    }

    private String readString() throws IOException, ParseException {
        consumeOrError('"');
        StringBuilder sb = new StringBuilder();
        while (!consume('"')) {
            if (consume('\\')) {
                switch (c) {
                    case '\\':
                    case '"':
                        break;
                    case 'n':
                        c = '\n';
                        break;
                    case 'r':
                        c = '\r';
                        break;
                    case 't':
                        c = '\t';
                        break;
                    case 'f':
                        c = '\f';
                        break;
                    case 'b':
                        c = '\b';
                        break;
                    case 'u':
                        readChar();
                        int ch = (readHexDigit() << 12)
                                + (readHexDigit() << 8)
                                + (readHexDigit() << 4)
                                + (readHexDigit());
                        sb.appendCodePoint(ch);
                        continue; // while()
                }
            }
            sb.appendCodePoint(c);
            readChar();
        }

        return sb.toString();
    }

    private String readToken() throws IOException {
        StringBuilder sb = new StringBuilder();
        while (!isSeparator(c) && !isWs(c)) {
            sb.appendCodePoint(c);
            readChar();
        }
        return sb.toString();
    }

    private JsonValue readValue() throws IOException, ParseException {
        switch (c) {
            case '"':
                return new JsonString(readString());
            case '[':
                return readArray();
            case '{':
                return readMap();
            case '+':
            case '-':
                return readNumber();
            case 't':
                return readTrue();
            case 'f':
                return readFalse();
            case 'n':
                readNull();
                return new JsonNull();
            default:
                if (c >= '0' && c <= '9') {
                    return readNumber();
                } else {
                    throw error("invalid value");
                }
        }
    }

    private JsonTrue readTrue() throws IOException, ParseException {
        if (!readToken().equals("true")) {
            throw error("invalid value");
        }
        return new JsonTrue();
    }

    private JsonFalse readFalse() throws IOException, ParseException {
        if (!readToken().equals("false")) {
            throw error("invalid value");
        }
        return new JsonFalse();
    }

    private void readNull() throws IOException, ParseException {
        if (!readToken().equals("null")) {
            throw error("invalid value");
        }
    }

    private void skipWs() throws IOException {
        while (isWs(c)) {
            readChar();
        }
    }

}
