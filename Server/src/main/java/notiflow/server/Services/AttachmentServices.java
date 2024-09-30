package notiflow.server.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.util.ByteArrayDataSource;
import notiflow.server.Config.S3ClientConfig;
import notiflow.server.Requests.EmailRequest;
import notiflow.server.Requests.S3Requests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class AttachmentServices {

    public final S3ClientConfig s3ClientConfig;

    @Autowired
    public AttachmentServices(S3ClientConfig s3ClientConfig) {
        this.s3ClientConfig = s3ClientConfig;
    }

    public EmailRequest uploadAndGetRequest(EmailRequest emailRequest) {
        S3Requests s3Requests = null;
        if (emailRequest.getAttachments() != null) {
            String BucketId = s3ClientConfig.createBucket("sumitdev1");
            try {
                s3Requests = s3ClientConfig.uploadFilesToS3(BucketId, emailRequest.getAttachments());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            emailRequest.setAttachments(null);
        }
        EmailRequest newRequest = new EmailRequest(emailRequest.getFromEmail(), emailRequest.getPassword(), emailRequest.getSubject(), emailRequest.getMessage(), emailRequest.getRecipients(), s3Requests, emailRequest.getTemplateImages(), emailRequest.getScheduleFutureMail());
        return newRequest;
    }

    public void handleAttachments(S3Requests s3Request, MimeMessageHelper helper) throws IOException, MessagingException {
        if (s3Request != null) {
            String bucketId = s3Request.getBucketID();
            List<String> objectKeys = s3Request.getS3ObjectKeys();

            for (String objectKey : objectKeys) {
                try (ResponseInputStream<GetObjectResponse> inputStream = s3ClientConfig.getObject(bucketId, objectKey)) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, bytesRead);
                    }

                    byte[] fileBytes = byteArrayOutputStream.toByteArray();

                    helper.addAttachment(objectKey, new ByteArrayDataSource(fileBytes, "application/octet-stream"));

                    s3ClientConfig.deleteObject(bucketId, objectKey);
                } catch (Exception e) {
                    throw new IOException("Failed to retrieve, attach, or delete file from S3: " + objectKey, e);
                }
            }

            // Delete the empty bucket after handling all attachments
            s3ClientConfig.deleteBucket(bucketId);
        }
    }


}
