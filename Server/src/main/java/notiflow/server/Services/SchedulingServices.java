package notiflow.server.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import notiflow.server.Requests.EmailRequest;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class SchedulingServices {

    private final Scheduler scheduler;
    private final ObjectMapper objectMapper;

    @Autowired
    public SchedulingServices(Scheduler scheduler, ObjectMapper objectMapper) {
        this.scheduler = scheduler;
        this.objectMapper = objectMapper;
    }

    public void validateScheduledDate(LocalDateTime scheduledDateTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime maxAllowedDate = now.plusDays(5);

        if (scheduledDateTime.isAfter(maxAllowedDate)) {
            throw new IllegalArgumentException("Scheduled date cannot be more than 5 days ahead");
        }
    }

    private String convertEmailRequestToJson(EmailRequest emailRequest) {
        try {
            return objectMapper.writeValueAsString(emailRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert EmailRequest to JSON", e);
        }
    }

    public void scheduleEmailJob(EmailRequest emailRequest, Class<? extends Job> jobClass, LocalDateTime scheduleFutureMail) {

        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(UUID.randomUUID().toString(), "email-jobs").usingJobData("emailRequestJson", convertEmailRequestToJson(emailRequest)).build();

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(UUID.randomUUID().toString(), "email-triggers").startAt(java.util.Date.from(scheduleFutureMail.atZone(ZoneId.systemDefault()).toInstant())).build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to schedule email", e);
        }
    }
}
