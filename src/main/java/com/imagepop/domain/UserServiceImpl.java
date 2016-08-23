package com.imagepop.domain;

import com.imagepop.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by BernardXie on 3/21/16.
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repo;

    @Transactional
    @Override
    public User registerNewUser(User user) throws EmailExistsException {
        if (emailExists(user.getEmail())) {
            throw new EmailExistsException("There is an account with the email address " + user.getEmail());
        }
        user.setPassword(SecurityConfig.encode(user.getPassword()));
        repo.save(user);

        return user;
    }

    private boolean emailExists(String email) {
        User user = repo.findByEmail(email);
        return user != null;
    }
}
