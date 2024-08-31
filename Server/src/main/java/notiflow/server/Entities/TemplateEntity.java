package notiflow.server.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.validator.constraints.URL;

@Entity
public class TemplateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @URL(message = "Cover Image URL should be valid")
    private String coverImageUrl;

    @URL(message = "Company Logo URL should be valid")
    private String companyLogoUrl;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "email_id")
    private EmailEntity emailEntity;

    public TemplateEntity() {
    }

    public TemplateEntity(String coverImageUrl, String companyLogoUrl) {
        this.coverImageUrl = coverImageUrl;
        this.companyLogoUrl = companyLogoUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @URL String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(@URL String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public @URL String getCompanyLogoUrl() {
        return companyLogoUrl;
    }

    public void setCompanyLogoUrl(@URL String companyLogoUrl) {
        this.companyLogoUrl = companyLogoUrl;
    }

    public EmailEntity getEmailEntity() {
        return emailEntity;
    }

    public void setEmailEntity(EmailEntity emailEntity) {
        this.emailEntity = emailEntity;
    }
}
