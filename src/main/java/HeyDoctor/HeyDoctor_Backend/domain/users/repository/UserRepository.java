package HeyDoctor.HeyDoctor_Backend.domain.users.repository;

import HeyDoctor.HeyDoctor_Backend.domain.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
