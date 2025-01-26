package clap.server.adapter.outbound.infrastructure.s3;

import clap.server.config.s3.KakaoS3Config;
import clap.server.domain.model.task.FilePath;
import clap.server.exception.S3Exception;
import clap.server.exception.code.S3Errorcode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploadService {
    private final KakaoS3Config kakaoS3Config;
    private final S3Client s3Client;

    public List<String> uploadFiles(FilePath filePrefix, List<MultipartFile> multipartFiles)  {
        return multipartFiles.stream().map((file) -> uploadSingleFileAndGetUrl(filePrefix, file)).toList();
    }

    private String uploadSingleFileAndGetUrl(FilePath filePrefix, MultipartFile file) {
        try {
            Path filePath = getFilePath(file);
            String objectKey = createObjectKey(filePrefix.getPath(), file.getOriginalFilename());
            uploadToS3(objectKey, filePath);
            Files.delete(filePath);
            return getFileUrl(objectKey);
        } catch (IOException e) {
            throw new S3Exception(S3Errorcode.FILE_UPLOAD_REQUEST_FAILED);
        }
    }

    private String getFileUrl(String objectKey) {
        return kakaoS3Config.getEndpoint() + "/v1/" + kakaoS3Config.getProjectId() + '/' + kakaoS3Config.getBucketName() + '/' + objectKey;
    }

    private static Path getFilePath(MultipartFile file) throws IOException {
        Path path = Files.createTempFile(null,null);
        Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
        return path;
    }

    private void uploadToS3(String filePath, Path path) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(kakaoS3Config.getBucketName())
                .key(filePath)
                .build();

        s3Client.putObject(putObjectRequest, path);
    }

    private String createFileId() {
        return UUID.randomUUID().toString();
    }

    private String createObjectKey(String filepath, String fileName) {
        String fileId = createFileId();
        return String.format("%s/%s-%s", filepath, fileId , fileName);
    }

}
