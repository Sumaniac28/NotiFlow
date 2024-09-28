package notiflow.server.Controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import notiflow.server.Requests.EmailRequest;
import notiflow.server.Services.EmailServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailServices emailService;
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);


    @Autowired
    public EmailController(EmailServices emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/sendMail")
    public ResponseEntity<String> sendEmail(@RequestBody @Valid EmailRequest emailRequest) {
        System.out.println(emailRequest);
        logger.info("Received EmailRequest: {}", emailRequest.toString()); // Log the request
        try {
            emailService.sendEmail(emailRequest);
            return ResponseEntity.ok("Email sent successfully!");
        } catch (MessagingException e) {
            return ResponseEntity.badRequest().body("Error sending email: " + e.getMessage());
        }
    }

    @PostMapping("/sendTemplateMail")
    public ResponseEntity<String> sendTemplateEmail(@Valid @RequestBody EmailRequest emailRequest) {
            try {
                emailService.sendTemplateEmail(emailRequest);
                return ResponseEntity.ok("Email sent successfully!");
            } catch (MessagingException e) {
                return ResponseEntity.badRequest().body("Error sending email: " + e.getMessage());
            }

    }

    @PostMapping("/sendAttachmentMail")
    public ResponseEntity<String> sendAttachmentEmail(@Valid @ModelAttribute EmailRequest emailRequest) {
        try{
            emailService.sendAttachmentMail(emailRequest);
            return ResponseEntity.ok("Email sent successfully!");
        }catch (MessagingException e) {
            return ResponseEntity.badRequest().body("Error sending email: " + e.getMessage());
        }
    }

    @PostMapping("/sendAttachmentMailWithTemplate")
    public ResponseEntity<String> sendAttachmentEmailWithTemplate(@Valid @ModelAttribute EmailRequest emailRequest) {
        try{
            emailService.sendAttachmentMailWithTemplate(emailRequest);
            return ResponseEntity.ok("Email sent successfully!");
        }catch (MessagingException e) {
            return ResponseEntity.badRequest().body("Error sending email: " + e.getMessage());
        }
    }

    @PostMapping("/schedule/sendMail")
    public ResponseEntity<String> scheduleEmail(@Valid @RequestBody EmailRequest emailRequest) {
        try {
            emailService.sendScheduledSimpleEmail(emailRequest);
            return ResponseEntity.ok("Email scheduled successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error scheduling email: " + e.getMessage());
        }
    }

    @PostMapping("/schedule/sendTemplateMail")
    public ResponseEntity<String> scheduleTemplateEmail(@Valid @RequestBody EmailRequest emailRequest) {
        try {
            emailService.sendScheduledTemplateEmail(emailRequest);
            return ResponseEntity.ok("Email scheduled successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error scheduling email: " + e.getMessage());
        }
    }

    @PostMapping("/schedule/sendAttachmentMail")
    public ResponseEntity<String> scheduleAttachmentEmail(@Valid @ModelAttribute EmailRequest emailRequest) {
        try {
            emailService.sendScheduledAttachmentEmail(emailRequest);
            return ResponseEntity.ok("Email scheduled successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error scheduling email: " + e.getMessage());
        }
    }

    @PostMapping("/schedule/sendAttachmentMailWithTemplate")
    public ResponseEntity<String> scheduleAttachmentEmailWithTemplate(@Valid @ModelAttribute EmailRequest emailRequest) {
        try {
            emailService.sendScheduledAttachmentEmailWithTemplate(emailRequest);
            return ResponseEntity.ok("Email scheduled successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error scheduling email: " + e.getMessage());
        }
    }
}
