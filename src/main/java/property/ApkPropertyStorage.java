package property;

import lombok.Getter;

public class ApkPropertyStorage implements MobSfApkProperty, AndrowarnApkProperty, SourceProperty {
    private final StringBuilder builder = new StringBuilder(7000);
    @Getter private int[] mobSfApkProperty;
    @Getter private int[] sourceImportProperty;
    @Getter private int[] androwarnApkProperty;
    @Getter private int[] sourceConstructorProperty;
    @Getter private int[] sourceMethodProperty;
    @Getter private int[] sourceCodeProperty;

    public String getCSVRepresentation() {
        if (mobSfApkProperty == null || sourceImportProperty == null || androwarnApkProperty == null
                || sourceConstructorProperty == null || sourceMethodProperty == null || sourceCodeProperty == null) {
            return null;
        }

        for (int property : mobSfApkProperty) {
            builder.append(property).append(',');
        }

        for (int property : sourceImportProperty) {
            builder.append(property).append(',');
        }

        for (int property : androwarnApkProperty) {
            builder.append(property).append(',');
        }

        for (int property : sourceConstructorProperty) {
            builder.append(property).append(',');
        }

        for (int property : sourceMethodProperty) {
            builder.append(property).append(',');
        }

        for (int i = 0; i < (sourceCodeProperty.length - 1); ++i) {
            builder.append(sourceCodeProperty[i]).append(',');
        }
        builder.append(sourceCodeProperty[sourceCodeProperty.length - 1]);
        return builder.toString();
    }

    @Override
    public void setMobSfProperties(int[] properties) {
        mobSfApkProperty = properties;
    }

    @Override
    public void setImportProperties(int[] properties) {
        sourceImportProperty = properties;
    }

    @Override
    public void setCodeProperties(int[] properties) {
        sourceCodeProperty = properties;
    }

    @Override
    public void setConstructorProperties(int[] properties) {
        sourceConstructorProperty = properties;
    }

    @Override
    public void setMethodProperties(int[] properties) {
        sourceMethodProperty = properties;
    }

    @Override
    public void setAndrowarnProperties(int[] properties) {
        androwarnApkProperty = properties;
    }
}
