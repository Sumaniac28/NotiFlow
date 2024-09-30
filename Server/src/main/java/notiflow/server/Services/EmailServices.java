package notiflow.server.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import notiflow.server.Config.MailSenderConfiguration;
import notiflow.server.Entities.EmailEntity;
import notiflow.server.Entities.TemplateEntity;
import notiflow.server.Entities.UserEntity;
import notiflow.server.Jobs.SimpleEmailJob;
import notiflow.server.Jobs.TemplateEmailJob;
import notiflow.server.Repository.EmailRepository;
import notiflow.server.Requests.EmailRequest;
import notiflow.server.Requests.RecipientRequest;
import notiflow.server.Utils.EmailUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailServices {

    private static final Logger logger = LoggerFactory.getLogger(EmailServices.class);

    private final EmailRepository emailRepository;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final UserServices userServices;
    private final AttachmentServices attachmentServices;
    private final TemplateServices templateServices;
    private final MailSenderConfiguration mailSenderConfiguration;
    private final SchedulingServices schedulingServices;

    @Autowired
    public EmailServices(EmailRepository emailRepository, JavaMailSender mailSender, @Qualifier("templateEngine") SpringTemplateEngine templateEngine, UserServices userServices, AttachmentServices attachmentServices, TemplateServices templateServices, MailSenderConfiguration mailSenderConfiguration, SchedulingServices schedulingServices) {
        this.emailRepository = emailRepository;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.userServices = userServices;
        this.attachmentServices = attachmentServices;
        this.templateServices = templateServices;
        this.mailSenderConfiguration = mailSenderConfiguration;
        this.schedulingServices = schedulingServices;
    }

    private void setRecipients(MimeMessageHelper helper, EmailRequest emailRequest) throws MessagingException {
        List<String> toRecipients = getRecipientsByType(emailRequest, "TO");
        if (!toRecipients.isEmpty()) {
            helper.setTo(toRecipients.toArray(new String[0]));
        }

        List<String> ccRecipients = getRecipientsByType(emailRequest, "CC");
        if (!ccRecipients.isEmpty()) {
            helper.setCc(ccRecipients.toArray(new String[0]));
        }

        List<String> bccRecipients = getRecipientsByType(emailRequest, "BCC");
        if (!bccRecipients.isEmpty()) {
            helper.setBcc(bccRecipients.toArray(new String[0]));
        }
    }

    private List<String> getRecipientsByType(EmailRequest emailRequest, String type) {
        return emailRequest.getRecipients().stream().filter(r -> type.equals(r.getType().toString())).map(RecipientRequest::getEmail).collect(Collectors.toList());
    }


    private void sendAndSaveEmail(JavaMailSender mailSender, MimeMessage mimeMessage, EmailEntity emailEntity, String recipient) throws MessagingException {
        try {
            mailSender.send(mimeMessage);
            emailEntity.setSent(true);
            logger.info("Email sent successfully to: {}", recipient);
        } catch (Exception e) {
            emailEntity.setSent(false);
            logger.error("Failed to send email to {}: {}", recipient, e.getMessage());
            throw new MessagingException("Failed to send email to " + recipient + ": " + e.getMessage());
        } finally {
            emailRepository.save(emailEntity);
        }
    }


    public void sendScheduledSimpleEmail(EmailRequest emailRequest) throws MessagingException {
        if (emailRequest.getScheduleFutureMail() == null) {
            throw new IllegalArgumentException("ScheduleFutureMail cannot be null");
        } else if (emailRequest.getScheduleFutureMail().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("ScheduleFutureMail cannot be in the past");
        } else {
            schedulingServices.validateScheduledDate(emailRequest.getScheduleFutureMail());
            schedulingServices.scheduleEmailJob(emailRequest, SimpleEmailJob.class, emailRequest.getScheduleFutureMail());
        }
    }

    public void sendScheduledTemplateEmail(EmailRequest emailRequest) throws MessagingException {
        if (emailRequest.getScheduleFutureMail() == null) {
            throw new IllegalArgumentException("ScheduleFutureMail cannot be null");
        } else if (emailRequest.getScheduleFutureMail().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("ScheduleFutureMail cannot be in the past");
        } else {
            schedulingServices.validateScheduledDate(emailRequest.getScheduleFutureMail());
            schedulingServices.scheduleEmailJob(emailRequest, TemplateEmailJob.class, emailRequest.getScheduleFutureMail());
        }
    }

    @Transactional
    public void sendEmail(EmailRequest emailRequest) throws MessagingException {
        UserEntity user = userServices.getUser(emailRequest.getFromEmail());
        JavaMailSender mailSender = mailSenderConfiguration.createJavaMailSender(emailRequest.getFromEmail(), emailRequest.getPassword());
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true); // true indicates multipart email
        sendSimpleEmail(emailRequest, user, mailSender, mimeMessage, helper);
    }

    @Transactional
    public void sendAttachmentMail(EmailRequest emailRequest) throws MessagingException {
        UserEntity user = userServices.getUser(emailRequest.getFromEmail());
        JavaMailSender mailSender = mailSenderConfiguration.createJavaMailSender(emailRequest.getFromEmail(), emailRequest.getPassword());
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        EmailRequest newRequest = attachmentServices.uploadAndGetRequest(emailRequest);
        sendSimpleEmail(newRequest, user, mailSender, mimeMessage, helper);
    }

    @Transactional
    public void sendTemplateEmail(EmailRequest emailRequest) throws MessagingException {
        UserEntity user = userServices.getUser(emailRequest.getFromEmail());
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        JavaMailSender mailSender = mailSenderConfiguration.createJavaMailSender(emailRequest.getFromEmail(), emailRequest.getPassword());
        TemplateEntity templateEntity = templateServices.saveTemplateEntity(emailRequest.getTemplateImages());
        EmailEntity emailEntity = EmailUtility.prepareEmailEntity(emailRequest, user, templateEntity);
        templateEntity.setEmailEntity(emailEntity);
        sendEmailWithTemplate(emailRequest, user, mailSender, mimeMessage, helper, emailEntity);
    }

    @Transactional
    public void sendAttachmentMailWithTemplate(EmailRequest emailRequest) throws MessagingException {
        UserEntity user = userServices.getUser(emailRequest.getFromEmail());
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        JavaMailSender mailSender = mailSenderConfiguration.createJavaMailSender(emailRequest.getFromEmail(), emailRequest.getPassword());
        TemplateEntity templateEntity = templateServices.saveTemplateEntity(emailRequest.getTemplateImages());
        EmailEntity emailEntity = EmailUtility.prepareEmailEntity(emailRequest, user, templateEntity);
        templateEntity.setEmailEntity(emailEntity);
        EmailRequest newRequest = attachmentServices.uploadAndGetRequest(emailRequest);
        sendEmailWithTemplate(newRequest, user, mailSender, mimeMessage, helper, emailEntity);
    }

    @Transactional
    public void sendScheduledAttachmentEmail(EmailRequest emailRequest) throws MessagingException {
        if (emailRequest.getScheduleFutureMail() == null) {
            throw new IllegalArgumentException("ScheduleFutureMail cannot be null");
        } else if (emailRequest.getScheduleFutureMail().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("ScheduleFutureMail cannot be in the past");
        } else {
            EmailRequest newRequest = attachmentServices.uploadAndGetRequest(emailRequest);
            schedulingServices.validateScheduledDate(newRequest.getScheduleFutureMail());
            schedulingServices.scheduleEmailJob(newRequest, SimpleEmailJob.class, emailRequest.getScheduleFutureMail());
        }
    }

    @Transactional
    public void sendScheduledAttachmentEmailWithTemplate(EmailRequest emailRequest) throws MessagingException {
        if (emailRequest.getScheduleFutureMail() == null) {
            throw new IllegalArgumentException("ScheduleFutureMail cannot be null");
        } else if (emailRequest.getScheduleFutureMail().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("ScheduleFutureMail cannot be in the past");
        } else {
            EmailRequest newRequest = attachmentServices.uploadAndGetRequest(emailRequest);
            schedulingServices.validateScheduledDate(newRequest.getScheduleFutureMail());
            schedulingServices.scheduleEmailJob(newRequest, TemplateEmailJob.class, emailRequest.getScheduleFutureMail());
        }
    }

    private void sendSimpleEmail(EmailRequest emailRequest, UserEntity user, JavaMailSender mailSender, MimeMessage mimeMessage, MimeMessageHelper helper) throws MessagingException {
        EmailEntity emailEntity = EmailUtility.prepareEmailEntity(emailRequest, user, null);
        setRecipients(helper, emailRequest);

        helper.setSubject(emailRequest.getSubject());
        helper.setText(emailRequest.getMessage());

        try {
            attachmentServices.handleAttachments(emailRequest.getS3Data(), helper);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String> recipients = getRecipientsByType(emailRequest, "TO");
        for (String recipient : recipients) {
            sendAndSaveEmail(mailSender, mimeMessage, emailEntity, recipient);
        }
    }

    private void sendEmailWithTemplate(EmailRequest emailRequest, UserEntity user, JavaMailSender mailSender, MimeMessage mimeMessage, MimeMessageHelper helper, EmailEntity emailEntity) throws MessagingException {
        Context context = new Context();
        context.setVariable("coverImageURL", emailRequest.getTemplateImages() != null ? emailRequest.getTemplateImages().getCoverImageUrl() : null);
        context.setVariable("message", emailRequest.getMessage());
        context.setVariable("year", java.time.Year.now().getValue());
        context.setVariable("companyLogoURL", emailRequest.getTemplateImages() != null ? emailRequest.getTemplateImages().getCompanyLogoUrl() : null);
        context.setVariable("companyName", user.getName());

        String htmlContent = templateEngine.process("email_template", context);
        setRecipients(helper, emailRequest);

        helper.setSubject(emailRequest.getSubject());
        helper.setText(htmlContent, true);

        try {
            attachmentServices.handleAttachments(emailRequest.getS3Data(), helper);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String> recipients = getRecipientsByType(emailRequest, "TO");
        for (String recipient : recipients) {
            sendAndSaveEmail(mailSender, mimeMessage, emailEntity, recipient);
        }
    }
}
