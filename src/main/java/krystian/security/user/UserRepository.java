package krystian.security.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * 12/13/2016 11:49 PM
 */
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findOneByLoginIgnoreCase(String username);
}