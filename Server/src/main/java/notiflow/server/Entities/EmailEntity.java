package notiflow.server.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

@Entity
public class EmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Email
    private String email;

    private String subject;

    @Size(min = 10, message = "Message should have at least 10 characters")
    private String message;

    @URL
    private String coverImageURL;

    @URL
    private String companyLogoURL;

    final static private String type = "email";

    private boolean isSent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @Email String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public @Size(min = 10, message = "Message should have at least 10 characters") String getMessage() {
        return message;
    }

    public void setMessage(@Size(min = 10, message = "Message should have at least 10 characters") String message) {
        this.message = message;
    }

    public @URL String getCoverImageURL() {
        return coverImageURL;
    }

    public void setCoverImageURL(@URL String coverImageURL) {
        this.coverImageURL = coverImageURL;
    }

    public @URL String getCompanyLogoURL() {
        return companyLogoURL;
    }

    public void setCompanyLogoURL(@URL String companyLogoURL) {
        this.companyLogoURL = companyLogoURL;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public EmailEntity() {
    }

    public EmailEntity(String email, String subject, String message) {
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.isSent = false;
    }

    public EmailEntity(String email, String subject, String message, @URL String coverImageURL, @URL String companyLogoURL) {
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.coverImageURL = coverImageURL;
        this.companyLogoURL = companyLogoURL;
        this.isSent = false;
    }

}
