package notiflow.server.Jobs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import notiflow.server.Requests.EmailRequest;
import notiflow.server.Services.EmailServices;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleEmailJob implements Job {

    private final EmailServices emailServices;
    private final ObjectMapper objectMapper;

    @Autowired
    public SimpleEmailJob(EmailServices emailServices, ObjectMapper objectMapper) {
        this.emailServices = emailServices;
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String emailRequestJson = jobExecutionContext.getJobDetail().getJobDataMap().getString("emailRequestJson");

        EmailRequest emailRequest;
        try {
            emailRequest = objectMapper.readValue(emailRequestJson, EmailRequest.class);
        } catch (JsonProcessingException e) {
            throw new JobExecutionException("Failed to deserialize EmailRequest from JSON", e);
        }

        try {
            emailServices.sendEmail(emailRequest);
        } catch (Exception e) {
            throw new JobExecutionException("Failed to send email", e);
        }
    }
}
