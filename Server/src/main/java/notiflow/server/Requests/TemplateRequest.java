package notiflow.server.Requests;

import org.hibernate.validator.constraints.URL;

import java.io.Serializable;

public class TemplateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @URL(message = "Cover Image URL should be valid")
    private String coverImageUrl;

    @URL(message = "Company Logo URL should be valid")
    private String companyLogoUrl;

    // Getters and setters
    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getCompanyLogoUrl() {
        return companyLogoUrl;
    }

    public void setCompanyLogoUrl(String companyLogoUrl) {
        this.companyLogoUrl = companyLogoUrl;
    }
}
