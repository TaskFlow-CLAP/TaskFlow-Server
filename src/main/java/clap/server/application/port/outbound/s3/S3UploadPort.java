package clap.server.application.port.outbound.s3;

import clap.server.domain.model.task.FilePath;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3UploadPort {
    List<String> uploadFiles(FilePath filePrefix, List<MultipartFile> multipartFiles);

    String uploadSingleFile(FilePath filePrefix, MultipartFile file);
}
