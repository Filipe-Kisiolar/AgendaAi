package kisiolar.filipe.Viviane.Ai.Cloud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class AWSConfig {
    @Value("${aws.region}")
    private String region;

    @Value("${storage.accessKey}")
    private String accessKey;

    @Value("${storage.secretKey}")
    private String secretKey;

    @Value("${storage.endpoint}")             // vazio p/ AWS S3; no R2 use https://<ACCOUNT_ID>.r2.cloudflarestorage.com
    private String customEndpoint;

    @Bean
    public S3Client createS3Instance(){

        boolean isCustomEndpoint = customEndpoint != null && !customEndpoint.isBlank();

        S3Configuration s3config = S3Configuration.builder()
                .pathStyleAccessEnabled(isCustomEndpoint)
                .build();

        S3ClientBuilder s3ClientBuilder = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(credentials(accessKey,secretKey))
                .serviceConfiguration(s3config);

        if(isCustomEndpoint){
            s3ClientBuilder = s3ClientBuilder.endpointOverride(URI.create(customEndpoint));
        }

        return s3ClientBuilder.build();
    }

    public AwsCredentialsProvider credentials(String accessKey, String secretKey){
        if (accessKey != null && !accessKey.isBlank() &&
                secretKey != null && !secretKey.isBlank()) {
            return StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
            );
        }
        return DefaultCredentialsProvider.create();
    }
}
