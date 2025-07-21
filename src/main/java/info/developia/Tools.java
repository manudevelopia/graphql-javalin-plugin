package info.developia;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Tools {

    public static String readResourceFile(String filepath) {
        try {
            return Files.readString(Path.of(Objects.requireNonNull(Tools.class.getClassLoader().getResource(filepath)).getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
