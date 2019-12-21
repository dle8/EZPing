package EZPing.authentication.domain.repository;

import EZPing.authentication.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}