package notiflow.server.Repository;

import notiflow.server.Entities.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailRepository extends JpaRepository<EmailEntity, Long> {

}
