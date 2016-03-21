package api.useraccounts;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by BernardXie on 3/21/16.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
