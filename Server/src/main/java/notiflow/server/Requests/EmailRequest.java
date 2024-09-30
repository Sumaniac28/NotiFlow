package notiflow.server.Requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class EmailRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email address is required")
    private String fromEmail;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Message is required")
    private String message;

    @NotEmpty(message = "Recipients are required")
    @JsonProperty("recipients")
    private List<RecipientRequest> recipients;

    private AttachmentRequest attachments;

    @JsonProperty("s3Data")
    private S3Requests S3Data;

    private TemplateRequest templateImages;

    private LocalDateTime scheduleFutureMail;

    public EmailRequest() {
    }

    public EmailRequest(String fromEmail, String password, String subject, String message, List<RecipientRequest> recipients, S3Requests S3Data, TemplateRequest templateImages, LocalDateTime scheduleFutureMail) {
        this.fromEmail = fromEmail;
        this.password = password;
        this.subject = subject;
        this.message = message;
        this.recipients = recipients;
        this.S3Data = S3Data;
        this.templateImages = templateImages;
        this.scheduleFutureMail = scheduleFutureMail;
    }

    // Getters and setters
    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<RecipientRequest> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<RecipientRequest> recipients) {
        this.recipients = recipients;
    }

    public AttachmentRequest getAttachments() {
        return attachments;
    }

    public void setAttachments(AttachmentRequest attachments) {
        this.attachments = attachments;
    }

    public S3Requests getS3Data() {
        return S3Data;
    }

    public void setS3Data(S3Requests S3Data) {
        this.S3Data = S3Data;
    }

    public TemplateRequest getTemplateImages() {
        return templateImages;
    }

    public void setTemplateImages(TemplateRequest templateRequest) {
        this.templateImages = templateRequest;
    }

    public LocalDateTime getScheduleFutureMail() {
        return scheduleFutureMail;
    }

    public void setScheduleFutureMail(LocalDateTime scheduleFutureMail) {
        this.scheduleFutureMail = scheduleFutureMail;
    }

    @Override
    public String toString() {
        return "EmailRequest{" + "fromEmail='" + fromEmail + '\'' + ", password='" + password + '\'' + ", subject='" + subject + '\'' + ", message='" + message + '\'' + ", recipients=" + recipients + ", attachments=" + attachments + ", S3Data=" + S3Data + ", templateImages=" + templateImages + ", scheduleFutureMail=" + scheduleFutureMail + '}';
    }
}
