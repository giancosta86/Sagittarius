package info.gianlucacosta.sagittarius.util;

import java.util.Objects;

public class Version {
    private static final int maxComponentsCount =
            4;


    public static Version parse(String source) {
        String[] componentStrings =
                source.split("\\.");

        Integer[] components =
                new Integer[maxComponentsCount];

        int componentsToInitializeCount =
                Math.min(
                        componentStrings.length,
                        maxComponentsCount
                );

        for (int componentIndex = 0; componentIndex < componentsToInitializeCount; componentIndex++) {
            components[componentIndex] =
                    Integer.parseInt(componentStrings[componentIndex]);
        }


        return new Version(
                components[0],
                components[1],
                components[2],
                components[3]
        );
    }


    private final Integer major;
    private final Integer minor;
    private final Integer build;
    private final Integer update;

    private String stringForm;


    public Version(Integer major, Integer minor, Integer build, Integer update) {
        Objects.requireNonNull(major);

        this.major = major;
        this.minor = minor;
        this.build = build;
        this.update = update;
    }


    public Integer getMajor() {
        return major;
    }

    public Integer getMinor() {
        return minor;
    }

    public Integer getBuild() {
        return build;
    }

    public Integer getUpdate() {
        return update;
    }


    public Version increase() {
        if (update != null) {
            return new Version(
                    major,
                    minor,
                    build,
                    update + 1
            );
        }

        if (build != null) {
            return new Version(
                    major,
                    minor,
                    build + 1,
                    null
            );
        }

        if (minor != null) {
            return new Version(
                    major,
                    minor + 1,
                    null,
                    null
            );
        }

        return new Version(
                major + 1,
                null,
                null,
                null
        );
    }


    @Override
    public String toString() {
        if (stringForm == null) {
            StringBuilder stringBuilder =
                    new StringBuilder();

            stringBuilder.append(major);

            if (minor != null) {
                stringBuilder.append(".");

                stringBuilder.append(minor);


                if (build != null) {
                    stringBuilder.append(".");
                    stringBuilder.append(build);


                    if (update != null) {
                        stringBuilder.append('.');
                        stringBuilder.append(update);
                    }
                }
            }

            stringForm =
                    stringBuilder.toString();
        }


        return stringForm;
    }
}
