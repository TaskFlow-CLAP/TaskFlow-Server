package clap.server.adapter.outbound.infrastructure.clamav;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import xyz.capybara.clamav.ClamavClient;
import xyz.capybara.clamav.ClamavException;
import xyz.capybara.clamav.commands.scan.result.ScanResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClamAVScanner {
    private final ClamavClient client;
    private final ThreadPoolTaskExecutor clamavExecutor;

    public CompletableFuture<Boolean> scanFileAsync(String filePath) {
        return CompletableFuture.supplyAsync(() -> {
            Path originalPath = Paths.get(filePath);
            Path tempPath = null;
            try {
                String originalFileName = originalPath.getFileName().toString();
                String fileExtension = getFileExtension(originalFileName);
                String tempFileName = "scan_" + UUID.randomUUID() + fileExtension;
                tempPath = Files.createTempFile("scan_", tempFileName);

                Files.copy(originalPath, tempPath, StandardCopyOption.REPLACE_EXISTING);

                ScanResult result = client.scan(tempPath);
                if (result instanceof ScanResult.OK) {
                    log.info("파일이 안전합니다: {}", originalFileName);
                    return true;
                } else if (result instanceof ScanResult.VirusFound virusFound) {
                    log.warn("바이러스 발견: {} in {}", virusFound.getFoundViruses(), originalFileName);
                    return false;
                }
                log.warn("알 수 없는 스캔 결과 타입: {}", result.getClass().getName());
                return false;
            } catch (IOException e) {
                log.error("파일 처리 중 오류 발생: {}", filePath, e);
                return false;
            } catch (ClamavException e) {
                log.error("ClamAV 스캔 중 오류 발생: {}", filePath, e);
                return false;
            } catch (Exception e) {
                log.error("예상치 못한 오류 발생: {}", filePath, e);
                return false;
            } finally {
                if (tempPath != null) {
                    try {
                        Files.deleteIfExists(tempPath);
                    } catch (IOException e) {
                        log.warn("임시 파일 삭제 실패: {}", tempPath, e);
                    }
                }
            }
        }, clamavExecutor);
    }

    private String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // 확장자가 없는 경우
        }
        return fileName.substring(lastIndexOf);
    }
}
