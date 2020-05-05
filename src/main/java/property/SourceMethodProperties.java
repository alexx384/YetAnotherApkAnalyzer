package property;

public enum SourceMethodProperties {
    INTENT_ADDFLAGS("Intent", "addFlags"),
    INTENT_SETFLAGS("Intent", "setFlags"),
    INTENT_SETDATAANDTYPE("Intent", "setDataAndType"),
    INTENT_PUTEXTRA("Intent", "putExtra"),
    INTENT_ADDCATEGORY("Intent", "addCategory"),
    DATAOUTPUTSTREAM_WRITEBYTES("DataOutputStream", "writeBytes"),
    STRINGBUILDER_APPEND("StringBuilder", "append"),
    STRINGBUILDER_INDEXOF("StringBuilder", "indexOf"),
    STRINGBUILDER_SUBSTRING("StringBuilder", "substring"),
    STRINGBUFFER_APPEND("StringBuffer", "append"),
    STRINGBUFFER_INDEXOF("StringBuffer", "indexOf"),
    STRINGBUFFER_SUBSTRING("StringBuffer", "substring"),
    CONTENTRESOLVER_QUERY("ContentResolver", "query"),
    CONTENTRESOLVER_INSERT("ContentResolver", "insert"),
    CONTENTRESOLVER_UPDATE("ContentResolver", "update"),
    STRING_TOLOWERCASE("String", "toLowerCase"),
    STRING_TRIM("String", "trim"),
    STRING_TOUPPERCASE("String", "toUpperCase"),
    STRING_CHARAT("String", "charAt");

    public static final int length = SourceMethodProperties.values().length;

    public final String type;
    public final String method;

    SourceMethodProperties(String type, String method) {
        this.type = type;
        this.method = method;
    }
}
