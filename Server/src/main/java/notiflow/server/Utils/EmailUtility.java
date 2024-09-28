package notiflow.server.Utils;

import notiflow.server.Entities.EmailEntity;
import notiflow.server.Entities.RecipientEntity;
import notiflow.server.Entities.TemplateEntity;
import notiflow.server.Entities.UserEntity;
import notiflow.server.Requests.EmailRequest;

import java.util.Set;
import java.util.stream.Collectors;

public class EmailUtility {

    public static EmailEntity prepareEmailEntity(EmailRequest emailRequest, UserEntity user, TemplateEntity templateEntity) {
        if (emailRequest.getSubject() == null || emailRequest.getSubject().isEmpty()) {
            throw new IllegalArgumentException("Subject cannot be null or empty");
        }
        if (emailRequest.getMessage() == null || emailRequest.getMessage().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }

        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setSubject(emailRequest.getSubject());
        emailEntity.setMessage(emailRequest.getMessage());
        emailEntity.setTemplateEntity(templateEntity);
        emailEntity.setUser(user);
        emailEntity.setSent(false);

        Set<RecipientEntity> recipientEntities = emailRequest.getRecipients().stream().map(r -> {
            RecipientEntity recipientEntity = new RecipientEntity();
            recipientEntity.setEmail(r.getEmail());
            recipientEntity.setType(r.getType());
            recipientEntity.setEmailEntity(emailEntity);
            return recipientEntity;
        }).collect(Collectors.toSet());

        emailEntity.setRecipients(recipientEntities);

        return emailEntity;
    }
}
