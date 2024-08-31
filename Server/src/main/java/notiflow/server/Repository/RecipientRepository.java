package notiflow.server.Repository;

import notiflow.server.Entities.RecipientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipientRepository extends JpaRepository<RecipientEntity, Integer> {
}
