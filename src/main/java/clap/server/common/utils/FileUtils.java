package clap.server.common.utils;

import org.apache.tika.Tika;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class FileUtils {
    private FileUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final Tika tika = new Tika();
    private static final List<String> VALID_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/pjpeg", "image/png", "image/gif", "image/bmp", "image/x-windows-bmp"
    );

    public static boolean validImageFile(InputStream inputStream) {
        try {
            String mimeType = tika.detect(inputStream);

            return VALID_IMAGE_TYPES.contains(mimeType.toLowerCase());
        } catch (IOException e) {
            return false;
        }
    }
}
