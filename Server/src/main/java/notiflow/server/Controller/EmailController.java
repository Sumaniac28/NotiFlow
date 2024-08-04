package notiflow.server.Controller;

import jakarta.mail.MessagingException;
import notiflow.server.Requests.EmailRequest;
import notiflow.server.Services.EmailServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailServices emailService;


    @PostMapping("/send")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        emailService.sendEmail(emailRequest.getToEmail(), emailRequest.getSubject(), emailRequest.getMessage());
        return "Email sent successfully!";
    }

    @PostMapping("/sendTemplate")
    public String sendTemplateEmail(@RequestBody EmailRequest emailRequest) {
        try {
            emailService.sendTemplateEmail(
                    emailRequest.getToEmail(),
                    emailRequest.getSubject(),
                    emailRequest.getMessage(),
                    emailRequest.getCoverImageURL(),
                    emailRequest.getCompanyLogoURL()
            );
            return "Template email sent successfully!";
        } catch (MessagingException e) {
            return "Failed to send template email: " + e.getMessage();
        }
    }
}
