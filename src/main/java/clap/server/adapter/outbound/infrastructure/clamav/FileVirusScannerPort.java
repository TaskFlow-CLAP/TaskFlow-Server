package clap.server.adapter.outbound.infrastructure.clamav;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileVirusScannerPort {
    List<MultipartFile> scanFiles(List<MultipartFile> files);
}
