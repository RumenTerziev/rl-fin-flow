package bg.lrsoft.rlfinflow.utils;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@UtilityClass
public class TestFileUtils {

    public static String readFileAsString(String stringPath) {
        Path filePath = Path.of(stringPath);
        try {
            return Files.readString(filePath);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Could not read file: " + stringPath, exception);
        }
    }
}
