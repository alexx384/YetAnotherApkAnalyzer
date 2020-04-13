package extract.mobsf.filetype;

public enum FileType {
    XML("xml"),
    PNG("png"),
    KOTLIN_METADATA("kotlin_metadata"),
    VERSION("version"),
    KOTLIN_BUILTINS("kotlin_builtins"),
    PRO("pro"),
    TTF("ttf"),
    DEX("dex"),
    GIF("gif"),
    OTHER("");

    public String value;

    FileType(String value) {
        this.value = value;
    }
}
