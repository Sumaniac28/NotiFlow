package notiflow.server.Entities;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class EmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "template_id")
    private TemplateEntity templateEntity;

    @Column(name = "is_sent", nullable = false)
    private boolean isSent;

    @OneToMany(mappedBy = "emailEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecipientEntity> recipients;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
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

    public TemplateEntity getTemplateEntity() {
        return templateEntity;
    }

    public void setTemplateEntity(TemplateEntity templateEntity) {
        this.templateEntity = templateEntity;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public Set<RecipientEntity> getRecipients() {
        return recipients;
    }

    public void setRecipients(Set<RecipientEntity> recipients) {
        this.recipients = recipients;
    }
}
