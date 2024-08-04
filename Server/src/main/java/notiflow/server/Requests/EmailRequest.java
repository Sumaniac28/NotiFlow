package notiflow.server.Requests;

public class EmailRequest {
    private String fromEmail;
    private String password;
    private String toEmail;
    private String subject;
    private String message;
    private String coverImageURL;
    private String companyLogoURL;

    // Default constructor
    public EmailRequest() {}

    // Parameterized constructor
    public EmailRequest(String fromEmail, String password, String toEmail, String subject, String message, String coverImageURL, String companyLogoURL) {
        this.fromEmail = fromEmail;
        this.password = password;
        this.toEmail = toEmail;
        this.subject = subject;
        this.message = message;
        this.coverImageURL = coverImageURL;
        this.companyLogoURL = companyLogoURL;
    }

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
