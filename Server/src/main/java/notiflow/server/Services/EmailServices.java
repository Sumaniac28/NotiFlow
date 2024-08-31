package notiflow.server.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import notiflow.server.Entities.EmailEntity;
import notiflow.server.Entities.RecipientEntity;
import notiflow.server.Entities.TemplateEntity;
import notiflow.server.Entities.UserEntity;
import notiflow.server.Repository.EmailRepository;
import notiflow.server.Repository.TemplateRepository;
import notiflow.server.Repository.UserRepository;
import notiflow.server.Requests.EmailRequest;
import notiflow.server.Requests.RecipientRequest;
import notiflow.server.Requests.TemplateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmailServices {

    private static final Logger logger = LoggerFactory.getLogger(EmailServices.class);

    private final EmailRepository emailRepository;
    private final UserRepository userRepository;
    private final TemplateRepository templateRepository;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public EmailServices(EmailRepository emailRepository, UserRepository userRepository, TemplateRepository templateRepository, JavaMailSender mailSender, @Qualifier("templateEngine") SpringTemplateEngine templateEngine) {
        this.emailRepository = emailRepository;
        this.userRepository = userRepository;
        this.templateRepository = templateRepository;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    private JavaMailSender createJavaMailSender(String fromEmail, String password) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(fromEmail);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    private UserEntity getUser(String fromEmail) {
        UserEntity user = userRepository.findByEmail(fromEmail);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }

    private TemplateEntity saveTemplateEntity(TemplateRequest templateRequest) {
        if (templateRequest == null) {
            return null;
        }
        TemplateEntity templateEntity = new TemplateEntity();
        templateEntity.setCoverImageUrl(templateRequest.getCoverImageUrl());
        templateEntity.setCompanyLogoUrl(templateRequest.getCompanyLogoUrl());

        return templateRepository.save(templateEntity);
    }

    private EmailEntity prepareEmailEntity(EmailRequest emailRequest, UserEntity user, TemplateEntity templateEntity) {
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

        Set<RecipientEntity> recipientEntities = emailRequest.getRecipients().stream()
                .map(r -> {
                    RecipientEntity recipientEntity = new RecipientEntity();
                    recipientEntity.setEmail(r.getEmail());
                    recipientEntity.setType(r.getType());
                    recipientEntity.setEmailEntity(emailEntity);
                    return recipientEntity;
                })
                .collect(Collectors.toSet());

        emailEntity.setRecipients(recipientEntities);

        return emailEntity;
    }

    @Transactional
    public void sendEmail(EmailRequest emailRequest) throws MessagingException {
        UserEntity user = getUser(emailRequest.getFromEmail());
        JavaMailSender mailSender = createJavaMailSender(emailRequest.getFromEmail(), emailRequest.getPassword());

        sendSimpleEmail(emailRequest, user, mailSender);
    }

    @Transactional
    public void sendTemplateEmail(EmailRequest emailRequest) throws MessagingException {
        UserEntity user = getUser(emailRequest.getFromEmail());
        JavaMailSender mailSender = createJavaMailSender(emailRequest.getFromEmail(), emailRequest.getPassword());

        TemplateEntity templateEntity = saveTemplateEntity(emailRequest.getTemplateImages());

        EmailEntity emailEntity = prepareEmailEntity(emailRequest, user, templateEntity);

        templateEntity.setEmailEntity(emailEntity);

        sendEmailWithTemplate(emailRequest, user, mailSender, emailEntity);
    }

    private void sendSimpleEmail(EmailRequest emailRequest, UserEntity user, JavaMailSender mailSender) throws MessagingException {
        EmailEntity emailEntity = prepareEmailEntity(emailRequest, user, null);

        List<String> recipients = emailRequest.getRecipients().stream()
                .filter(r -> "TO".equals(r.getType().toString()))
                .map(RecipientRequest::getEmail)
                .collect(Collectors.toList());

        for (String recipient : recipients) {
            if (recipient == null || recipient.isEmpty()) {
                logger.warn("Skipping empty recipient");
                continue;
            }

            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(recipient);
            email.setSubject(emailRequest.getSubject());
            email.setText(emailRequest.getMessage());

            List<String> ccRecipients = emailRequest.getRecipients().stream()
                    .filter(r -> "CC".equals(r.getType().toString()))
                    .map(RecipientRequest::getEmail)
                    .collect(Collectors.toList());
            if (!ccRecipients.isEmpty()) {
                email.setCc(ccRecipients.toArray(new String[0]));
            }

            List<String> bccRecipients = emailRequest.getRecipients().stream()
                    .filter(r -> "BCC".equals(r.getType().toString()))
                    .map(RecipientRequest::getEmail)
                    .collect(Collectors.toList());
            if (!bccRecipients.isEmpty()) {
                email.setBcc(bccRecipients.toArray(new String[0]));
            }

            try {
                logger.info("Sending simple email to: {}", recipient);
                mailSender.send(email);
                emailEntity.setSent(true);
                logger.info("Simple email sent successfully to: {}", recipient);
            } catch (Exception e) {
                emailEntity.setSent(false);
                logger.error("Failed to send simple email to {}: {}", recipient, e.getMessage());
                throw new MessagingException("Failed to send simple email to " + recipient + ": " + e.getMessage());
            } finally {
                emailRepository.save(emailEntity);
            }
        }
    }

    private void sendEmailWithTemplate(EmailRequest emailRequest, UserEntity user, JavaMailSender mailSender, EmailEntity emailEntity) throws MessagingException {
        Context context = new Context();
        context.setVariable("coverImageURL", emailRequest.getTemplateImages() != null ? emailRequest.getTemplateImages().getCoverImageUrl() : null);
        context.setVariable("message", emailRequest.getMessage());
        context.setVariable("year", java.time.Year.now().getValue());
        context.setVariable("companyLogoURL", emailRequest.getTemplateImages() != null ? emailRequest.getTemplateImages().getCompanyLogoUrl() : null);
        context.setVariable("companyName", user.getName());

        String htmlContent = templateEngine.process("email_template", context);

        List<String> recipients = emailRequest.getRecipients().stream()
                .filter(r -> "TO".equals(r.getType().toString()))
                .map(RecipientRequest::getEmail)
                .collect(Collectors.toList());

        for (String recipient : recipients) {
            if (recipient == null || recipient.isEmpty()) {
                logger.warn("Skipping empty recipient");
                continue;
            }

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(recipient);
            helper.setSubject(emailRequest.getSubject());
            helper.setText(htmlContent, true);

            List<String> ccRecipients = emailRequest.getRecipients().stream()
                    .filter(r -> "CC".equals(r.getType().toString()))
                    .map(RecipientRequest::getEmail)
                    .collect(Collectors.toList());
            if (!ccRecipients.isEmpty()) {
                helper.setCc(ccRecipients.toArray(new String[0]));
            }

            List<String> bccRecipients = emailRequest.getRecipients().stream()
                    .filter(r -> "BCC".equals(r.getType().toString()))
                    .map(RecipientRequest::getEmail)
                    .collect(Collectors.toList());
            if (!bccRecipients.isEmpty()) {
                helper.setBcc(bccRecipients.toArray(new String[0]));
            }

            try {
                logger.info("Sending template email to: {}", recipient);
                mailSender.send(mimeMessage);
                emailEntity.setSent(true);
                logger.info("Template email sent successfully to: {}", recipient);
            } catch (Exception e) {
                emailEntity.setSent(false);
                logger.error("Failed to send template email to {}: {}", recipient, e.getMessage());
                throw new MessagingException("Failed to send template email to " + recipient + ": " + e.getMessage());
            } finally {
                emailRepository.save(emailEntity);
            }
        }
    }
}
