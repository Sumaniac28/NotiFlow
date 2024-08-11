package notiflow.server.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import notiflow.server.Entities.EmailEntity;
import notiflow.server.Entities.UserEntity;
import notiflow.server.Repository.EmailRepository;
import notiflow.server.Repository.UserRepository;
import notiflow.server.Requests.EmailRequest;
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

import java.util.Properties;

@Service
public class EmailServices {

    private static final Logger logger = LoggerFactory.getLogger(EmailServices.class);

    private final EmailRepository emailRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public EmailServices(EmailRepository emailRepository, UserRepository userRepository, JavaMailSender mailSender, @Qualifier("templateEngine") SpringTemplateEngine templateEngine) {
        this.emailRepository = emailRepository;
        this.userRepository = userRepository;
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

    private EmailEntity prepareEmailEntity(EmailRequest emailRequest, UserEntity user, String recipient) {
        if (emailRequest.getSubject() == null || emailRequest.getSubject().isEmpty()) {
            throw new IllegalArgumentException("Subject cannot be null or empty");
        }
        if (emailRequest.getMessage() == null || emailRequest.getMessage().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }

        if (emailRequest.getCcEmail() == null) {
            emailRequest.setCcEmail("");
        }

        if (emailRequest.getBccEmail() == null) {
            emailRequest.setBccEmail("");
        }

        return new EmailEntity(
                recipient,
                emailRequest.getCcEmail(),
                emailRequest.getBccEmail(),
                emailRequest.getSubject(),
                emailRequest.getMessage(),
                emailRequest.getCoverImageURL(),
                emailRequest.getCompanyLogoURL(),
                user
        );
    }


    public void sendEmail(EmailRequest emailRequest) throws MessagingException {
        UserEntity user = getUser(emailRequest.getFromEmail());
        JavaMailSender mailSender = createJavaMailSender(emailRequest.getFromEmail(), emailRequest.getPassword());

        sendSimpleEmail(emailRequest, user, mailSender, emailRequest.getToEmail().toArray(new String[0]));
    }

    public void sendTemplateEmail(EmailRequest emailRequest) throws MessagingException {
        UserEntity user = getUser(emailRequest.getFromEmail());
        JavaMailSender mailSender = createJavaMailSender(emailRequest.getFromEmail(), emailRequest.getPassword());

        sendEmailWithTemplate(emailRequest, user, mailSender, emailRequest.getToEmail().toArray(new String[0]));
    }

    private void sendSimpleEmail(EmailRequest emailRequest, UserEntity user, JavaMailSender mailSender, String[] recipients) throws MessagingException {
        for (String recipient : recipients) {
            if (recipient == null || recipient.isEmpty()) {
                logger.warn("Skipping empty recipient");
                continue;
            }

            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(recipient);
            email.setSubject(emailRequest.getSubject());
            email.setText(emailRequest.getMessage());

            if (!emailRequest.getCcEmail().isEmpty()) {
                email.setCc(emailRequest.getCcEmail());
            }

            if (!emailRequest.getBccEmail().isEmpty()) {
                email.setBcc(emailRequest.getBccEmail());
            }

            EmailEntity emailEntity = prepareEmailEntity(emailRequest, user, recipient);

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
                user.setEmails(emailEntity);
            }
        }
        userRepository.save(user);
    }

    private void sendEmailWithTemplate(EmailRequest emailRequest, UserEntity user, JavaMailSender mailSender, String[] recipients) throws MessagingException {
        Context context = new Context();
        context.setVariable("coverImageURL", emailRequest.getCoverImageURL());
        context.setVariable("message", emailRequest.getMessage());
        context.setVariable("year", java.time.Year.now().getValue());
        context.setVariable("companyLogoURL", emailRequest.getCompanyLogoURL());
        context.setVariable("companyName", user.getName());

        String htmlContent = templateEngine.process("email_template", context);

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

            if ( emailRequest.getCcEmail()!=null && !emailRequest.getCcEmail().isEmpty()) {
                helper.setCc(emailRequest.getCcEmail());
            }

            if (  emailRequest.getBccEmail()!=null && !emailRequest.getBccEmail().isEmpty()) {
                helper.setBcc(emailRequest.getBccEmail());
            }

            EmailEntity emailEntity = prepareEmailEntity(emailRequest, user, recipient);

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
                user.setEmails(emailEntity);
            }
        }
        userRepository.save(user);
    }


}
