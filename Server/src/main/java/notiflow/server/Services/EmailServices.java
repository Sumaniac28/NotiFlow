package notiflow.server.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;


@Service
public class EmailServices {

    @Autowired
    private JavaMailSender mailSender;

    private JavaMailSender createJavaMailSender(String fromEmail, String password) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.example.com"); // Update with the correct SMTP host
        mailSender.setPort(587); // Update with the correct SMTP port

        mailSender.setUsername(fromEmail);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    public void sendEmail(String toEmail, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(toEmail);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }

    public void sendTemplateEmail(String toEmail, String subject, String message, String coverImageURL, String companyLogoURL) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String htmlContent = "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; margin: 0; padding: 0; }" +
                ".email-container { max-width: 600px; margin: auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #ffffff; }" +
                ".header { text-align: center; margin-bottom: 20px; }" +
                ".cover-image { width: 100%; height: auto; border-radius: 8px; }" +
                ".content { margin: 20px 0; }" +
                ".content p { margin: 0; padding: 0; }" +
                ".footer { margin-top: 30px; text-align: center; font-size: 12px; color: #999; border-top: 1px solid #e0e0e0; padding-top: 10px; }" +
                ".footer img { max-width: 150px; margin-top: 10px; }" +
                ".footer p { margin: 0; padding: 0; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='email-container'>" +
                "<div class='header'>" +
                "<img src='" + coverImageURL + "' alt='Cover Image' class='cover-image' />" +
                "</div>" +
                "<div class='content'>" +
                "<p>" + message + "</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>&copy; " + java.time.Year.now() + " Your Company. All rights reserved.</p>" +
                "<img src='" + companyLogoURL + "' alt='Company Logo' />" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true indicates HTML content

        mailSender.send(mimeMessage);
    }
}