package notiflow.server.Repository;

import notiflow.server.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    UserEntity findByEmail(String email);
    UserEntity findByPhone(String phone);
}
