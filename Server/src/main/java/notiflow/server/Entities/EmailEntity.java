package notiflow.server.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

@Entity
public class EmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Email
    private String email;

    @Email
    private String ccEmail;

    @Email
    private String bccEmail;

    private String subject;

    @Size(min = 10, message = "Message should have at least 10 characters")
    private String message;

    @URL
    private String coverImageURL;

    @URL
    private String companyLogoURL;

    final static private String type = "email";

    private boolean isSent;

    private LocalDate date;

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public EmailEntity() {
    }

    public EmailEntity(String email, String ccEmail, String bccEmail, String subject, String message, UserEntity user) {
        this.email = email;
        this.ccEmail = ccEmail;
        this.bccEmail = bccEmail;
        this.subject = subject;
        this.message = message;
        this.user = user;
        this.isSent = false;
        this.date = LocalDate.now();
    }

    public EmailEntity(String email, String ccEmail, String bccEmail, String subject, String message, @URL String coverImageURL, @URL String companyLogoURL, UserEntity user) {
        this.email = email;
        this.ccEmail = ccEmail;
        this.bccEmail = bccEmail;
        this.subject = subject;
        this.message = message;
        this.coverImageURL = coverImageURL;
        this.companyLogoURL = companyLogoURL;
        this.user = user;
        this.isSent = false;
        this.date = LocalDate.now();
    }

}
