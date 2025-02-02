package clap.server.config.s3;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class KakaoS3Config {

    @Getter
    private final String projectId;
    @Getter
    private final String endpoint;
    private final String accessKey;
    private final String secretKey;
    private final String region;
    @Getter
    private String bucketName;

    public KakaoS3Config(
            @Value("${cloud.kakao.project-id}") String projectId,
            @Value("${cloud.kakao.object-storage.endpoint}") String endpoint,
            @Value("${cloud.kakao.object-storage.access-key}") String accessKey,
            @Value("${cloud.kakao.object-storage.secret-key}") String secretKey,
            @Value("${cloud.kakao.region}") String region,
            @Value("${cloud.kakao.object-storage.bucket-name}") String bucketName
    ) {
        this.projectId = projectId;
        this.endpoint = endpoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.region = region;
        this.bucketName = bucketName;
    }


    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .endpointOverride(URI.create(endpoint))
                .forcePathStyle(true)
                .build();
    }
}
