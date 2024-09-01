package notiflow.server.Requests;

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
    private List<RecipientRequest> recipients;

    private TemplateRequest templateImages;
    private LocalDateTime scheduleFutureMail;

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
}
