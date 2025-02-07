package clap.server.application.port.outbound.s3;

import clap.server.domain.policy.attachment.FilePathPolicyConstants;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3UploadPort {
    List<String> uploadFiles(FilePathPolicyConstants filePrefix, List<MultipartFile> multipartFiles);

    String uploadSingleFile(FilePathPolicyConstants filePrefix, MultipartFile file);
}
