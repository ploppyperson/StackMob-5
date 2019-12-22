package uk.antiperson.stackmob.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;

public class Utilities {

    public static CompletableFuture<DownloadResult> downloadFile(File filePath, String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL webPath = new URL(url);
                try (InputStream in = webPath.openStream()) {
                    Files.createDirectories(filePath.getParentFile().toPath());
                    Files.copy(in, filePath.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                return DownloadResult.SUCCESSFUL;
            } catch (IOException e) {
                e.printStackTrace();
                return DownloadResult.ERROR;
            }
        });
    }

    public static boolean isPaper() {
        return Package.getPackage("com.destroystokyo.paper") != null;
    }

    public enum DownloadResult {
        SUCCESSFUL,
        ERROR
    }
}
