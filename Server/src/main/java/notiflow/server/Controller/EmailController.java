package notiflow.server.Controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import notiflow.server.Requests.EmailRequest;
import notiflow.server.Services.EmailServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailServices emailService;

    @Autowired
    public EmailController(EmailServices emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailRequest emailRequest) {
        try{
            emailService.sendEmail(emailRequest);
            return ResponseEntity.ok("Email sent successfully!");
        } catch (MessagingException e) {
            return ResponseEntity.badRequest().body("Error sending email: " + e.getMessage());
        }
    }

    @PostMapping("/sendTemplate")
    public ResponseEntity<String> sendTemplateEmail(@Valid @RequestBody EmailRequest emailRequest) {
            try {
                emailService.sendTemplateEmail(emailRequest);
                return ResponseEntity.ok("Email sent successfully!");
            } catch (MessagingException e) {
                return ResponseEntity.badRequest().body("Error sending email: " + e.getMessage());
            }

    }
}
