package notiflow.server.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import notiflow.server.Enums.RecipientType;

@Entity
public class RecipientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "recipient_type", nullable = false)
    private RecipientType type;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "email_id")
    private EmailEntity emailEntity;


    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RecipientType getType() {
        return type;
    }

    public void setType(RecipientType type) {
        this.type = type;
    }

    public EmailEntity getEmailEntity() {
        return emailEntity;
    }

    public void setEmailEntity(EmailEntity emailEntity) {
        this.emailEntity = emailEntity;
    }
}
