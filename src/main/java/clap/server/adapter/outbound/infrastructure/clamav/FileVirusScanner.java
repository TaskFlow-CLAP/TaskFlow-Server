package clap.server.adapter.outbound.infrastructure.clamav;

import clap.server.exception.AdapterException;
import clap.server.exception.ClamAVException;
import clap.server.exception.code.FileErrorcode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileVirusScanner implements FileVirusScannerPort {
    private final ClamAVScanner clamAVScanner;

    public List<MultipartFile> scanFiles(List<MultipartFile> files) {
        List<CompletableFuture<MultipartFile>> scanResults = files.stream()
                .map(this::scanFile)
                .toList();

        CompletableFuture.allOf(scanResults.toArray(new CompletableFuture[0])).join();

        return scanResults.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public MultipartFile scanSingleFile(MultipartFile file) throws ExecutionException, InterruptedException {
        return scanFile(file).get();
    }

    @Async("clamavExecutor")
    protected CompletableFuture<MultipartFile> scanFile(MultipartFile file) {
        return CompletableFuture.supplyAsync(() -> {
            Path tempPath = null;
            try {
                tempPath = Files.createTempFile("scan_", "_" + file.getOriginalFilename());
                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, tempPath, StandardCopyOption.REPLACE_EXISTING);
                }
                clamAVScanner.scanFileAsync(tempPath.toString()).get();
                return file;
            } catch (ClamAVException e) {
                log.warn("Virus detected in file: {}", file.getOriginalFilename());
                throw new AdapterException(FileErrorcode.FILE_SCAN_FAILED);
            } catch (Exception e) {
                log.error("Failed to scan file: {}", file.getOriginalFilename(), e);
                return null;
            } finally {
                if (tempPath != null) {
                    try {
                        Files.deleteIfExists(tempPath);
                    } catch (IOException e) {
                        log.error("Failed to delete temp file: {}", tempPath, e);
                    }
                }
            }
        });
    }
}
