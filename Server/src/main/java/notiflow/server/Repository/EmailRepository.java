package notiflow.server.Repository;

import notiflow.server.Entities.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<EmailEntity, Integer> {
}
