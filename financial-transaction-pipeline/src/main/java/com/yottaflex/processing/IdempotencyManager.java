
package com.yottaflex.processing;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.List;

public class IdempotencyManager {

    private String registryPath = "data/output/processed_files.txt";

    private String generateFileKey(String filePath) throws Exception {

        Path path = Paths.get(filePath);

        byte[] fileContent = Files.readAllBytes(path);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] hash = digest.digest(fileContent);

        StringBuilder result = new StringBuilder();

        for (byte b : hash) {
            result.append(String.format("%02x", b));
        }

        return path.toAbsolutePath().normalize().toString()
                + "|" + result.toString();
    }

    public boolean hasAlreadyProcessed(String filePath) throws Exception {

        File registry = new File(registryPath);

        if (!registry.exists()) {
            return false;
        }

        String fileKey = generateFileKey(filePath);

        List<String> processedFiles = Files.readAllLines(
                registry.toPath()
        );

        return processedFiles.contains(fileKey);
    }

    public void markAsProcessed(String filePath) throws Exception {

        Path registry = Paths.get(registryPath);

        Files.createDirectories(registry.getParent());

        String fileKey = generateFileKey(filePath);

        Files.write(
                registry,
                (fileKey + System.lineSeparator()).getBytes("UTF-8"),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }
}
