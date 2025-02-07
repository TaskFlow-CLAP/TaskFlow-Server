package clap.server.adapter.outbound.infrastructure.clamav;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface FileVirusScannerPort {
    List<MultipartFile> scanFiles(List<MultipartFile> files);
    MultipartFile scanSingleFile(MultipartFile file) throws ExecutionException, InterruptedException;
}
