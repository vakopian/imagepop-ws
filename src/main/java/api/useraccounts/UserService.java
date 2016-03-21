package api.useraccounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by BernardXie on 3/21/16.
 */

@Service
public class UserService implements IUserService{
    @Autowired
    private UserRepository repo;

    @Transactional
    @Override
    public User registerNewUser(User user) throws EmailExistsException {
        if (emailExists(user.getEmail())) {
            throw new EmailExistsException("There is an account with the email address " + user.getEmail());
        }
        User account = new User();
        account.setFirstName(user.getFirstName());

        return repo.save(user);
    }

    private boolean emailExists(String email) {
        User user = repo.findByEmail(email);
        if(user != null) {
            return true;
        }
        return false;
    }
}
