package clap.server.application.port.outbound.s3;

import clap.server.domain.policy.attachment.FilePathPolicy;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3UploadPort {
    List<String> uploadFiles(FilePathPolicy filePrefix, List<MultipartFile> multipartFiles);

    String uploadSingleFile(FilePathPolicy filePrefix, MultipartFile file);
}
