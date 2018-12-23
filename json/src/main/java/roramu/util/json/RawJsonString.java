package roramu.util.json;

/**
 * A wrapper around a string which is already valid JSON.  This string will be serialized as-is (without surrounding
 * the string in quotation marks) when using {@link JsonUtils#write}.
 */
public final class RawJsonString {
    private String value;

    public RawJsonString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
