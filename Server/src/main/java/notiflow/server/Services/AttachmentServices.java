package notiflow.server.Services;

import jakarta.mail.MessagingException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AttachmentServices {
    public void processAttachments(MimeMessageHelper helper, MultipartFile[] files, String category) throws IOException {
        if (files != null) {
            for (MultipartFile file : files) {
                System.out.println("Processing " + category + ": " + file.getOriginalFilename());
                try {
                    helper.addAttachment(file.getOriginalFilename(), file);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
