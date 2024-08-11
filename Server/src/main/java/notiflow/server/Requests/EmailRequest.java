package notiflow.server.Requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class EmailRequest {

    @Email(message = "From Email should be valid")
    @NotBlank(message = "From Email is mandatory")
    private String fromEmail;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotEmpty(message = "To Email list cannot be empty")
    private List<@NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") String> toEmail;


    @Email(message = "CC Email should be valid")
    private String ccEmail;

    @Email(message = "BCC Email should be valid")
    private String bccEmail;

    private String subject;

    private String message;

    private String coverImageURL;

    private String companyLogoURL;

    public EmailRequest() {}

    public EmailRequest(String fromEmail, String password,List<String> toEmail, String subject, String message, String coverImageURL, String companyLogoURL, String ccEmail, String bccEmail) {
        this.fromEmail = fromEmail;
        this.password = password;
        this.toEmail = toEmail;
        this.ccEmail = ccEmail;
        this.bccEmail = bccEmail;
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

    public List<String> getToEmail() {
        return toEmail;
    }

    public void setToEmail(List<String> toEmail) {
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

    public String getCcEmail() {
        return ccEmail;
    }

    public void setCcEmail(String ccEmail) {
        this.ccEmail = ccEmail;
    }

    public String getBccEmail() {
        return bccEmail;
    }

    public void setBccEmail (String bccEmail) {
        this.bccEmail = bccEmail;
    }

}
