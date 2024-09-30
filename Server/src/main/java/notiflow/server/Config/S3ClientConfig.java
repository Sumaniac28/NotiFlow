package notiflow.server.Config;

import notiflow.server.Requests.AttachmentRequest;
import notiflow.server.Requests.S3Requests;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class S3ClientConfig {
    public static final String ACCESS_KEY = "";
    public static final String SECRET_KEY = "";
    private final S3Client s3Client;

    public S3ClientConfig() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
        this.s3Client = S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }

    public String createBucket(String Name) {
        String bucketName = Name + "-" + "email-attachment";
        s3Client.createBucket(b -> b.bucket(bucketName));
        return bucketName;
    }

    public S3Requests uploadFilesToS3(String BucketId, AttachmentRequest attachments) throws IOException {
        List<String> objectKeys = new ArrayList<>();

        objectKeys.addAll(uploadFileArray(BucketId, "images", attachments.getImages()));
        objectKeys.addAll(uploadFileArray(BucketId, "pdfs", attachments.getPdfs()));
        objectKeys.addAll(uploadFileArray(BucketId, "docs", attachments.getDocs()));
        objectKeys.addAll(uploadFileArray(BucketId, "others", attachments.getOther()));

        S3Requests s3Requests = new S3Requests();
        s3Requests.setBucketID(BucketId);
        s3Requests.setS3ObjectKeys(objectKeys);
        return s3Requests;
    }

    private List<String> uploadFileArray(String bucketId, String prefix, MultipartFile[] files) throws IOException {
        List<String> keys = new ArrayList<>();

        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    keys.add(uploadSingleFile(bucketId, prefix, file));
                }
            }
        }
        return keys;
    }

    private String uploadSingleFile(String bucketId, String prefix, MultipartFile file) throws IOException {
        String key = prefix + "/" + file.getOriginalFilename();

        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucketId)
                    .key(key)
                    .build(), software.amazon.awssdk.core.sync.RequestBody.fromInputStream(inputStream, file.getSize()));
        }

        return key;
    }

    public ResponseInputStream<GetObjectResponse> getObject(String bucketName, String key) {
        return s3Client.getObject(b -> b.bucket(bucketName).key(key));
    }

    public void deleteBucket(String bucketName) {
        s3Client.deleteBucket(b -> b.bucket(bucketName));
    }

    public void deleteObject(String bucketId, String objectKey) {
        s3Client.deleteObject(b -> b.bucket(bucketId).key(objectKey));
    }
}
