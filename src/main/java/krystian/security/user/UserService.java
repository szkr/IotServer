package krystian.security.user;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserByEmail(String email);
}