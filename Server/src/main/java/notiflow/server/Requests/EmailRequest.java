package notiflow.server.Requests;

import java.util.List;

public class EmailRequest {

    private String fromEmail;
    private String password;
    private String subject;
    private String message;
    private List<RecipientRequest> recipients;
    private TemplateRequest templateImages;

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
}
