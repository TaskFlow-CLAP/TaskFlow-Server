package clap.server.application.port.outbound.s3;

import clap.server.common.constants.FilePathConstants;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3UploadPort {
    List<String> uploadFiles(FilePathConstants filePrefix, List<MultipartFile> multipartFiles);

    String uploadSingleFile(FilePathConstants filePrefix, MultipartFile file);
}
