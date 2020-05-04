package property;

import lombok.Getter;

public class ApkPropertyStorage {
    @Getter private final MobSfApkProperty mobSfApkProperty;
    @Getter private final SourceImportJavaProperty sourceImportJavaProperty;
    @Getter private final AndrowarnApkProperty androwarnApkProperty;
    @Getter private final SourceApiJavaProperty sourceApiJavaProperty;
    @Getter private final SourceCodeJavaProperty sourceCodeJavaProperty;

    public ApkPropertyStorage() {
        androwarnApkProperty = new AndrowarnApkProperty();
        mobSfApkProperty = new MobSfApkProperty();
        sourceImportJavaProperty = new SourceImportJavaProperty();
        sourceApiJavaProperty = new SourceApiJavaProperty();
        sourceCodeJavaProperty = new SourceCodeJavaProperty();
    }

    public String getCSVRepresentation() {
        StringBuilder builder = new StringBuilder(512);
        mobSfApkProperty.toBuilder(builder).append(',');
        sourceImportJavaProperty.toBuilder(builder).append(',');
        androwarnApkProperty.toBuilder(builder).append(',');
        sourceApiJavaProperty.toBuilder(builder).append(',');
        sourceCodeJavaProperty.toBuilder(builder);
        return builder.toString();
    }
}
