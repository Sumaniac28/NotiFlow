package notiflow.server.Requests;

public class EmailRequest {
    private String toEmail;
    private String subject;
    private String message;
    private String coverImageURL;
    private String companyLogoURL;

    // Default constructor
    public EmailRequest() {}

    // Parameterized constructor
    public EmailRequest(String toEmail, String subject, String message, String coverImageURL, String companyLogoURL) {
        this.toEmail = toEmail;
        this.subject = subject;
        this.message = message;
        this.coverImageURL = coverImageURL;
        this.companyLogoURL = companyLogoURL;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
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

    public String getCoverImageURL() {
        return coverImageURL;
    }

    public void setCoverImageURL(String coverImageURL) {
        this.coverImageURL = coverImageURL;
    }

    public String getCompanyLogoURL() {
        return companyLogoURL;
    }

    public void setCompanyLogoURL(String companyLogoURL) {
        this.companyLogoURL = companyLogoURL;
    }
}
