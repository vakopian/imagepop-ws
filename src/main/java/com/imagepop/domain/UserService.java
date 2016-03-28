package com.imagepop.domain;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    public User registerNewUser(User userInfo) throws EmailExistsException { //Figure out the DTO stuff
        if (emailExists(userInfo.getEmail())) {
            throw new EmailExistsException("There is an account with the email address " + userInfo.getEmail());
        }
        User account = new User();
        account.setFirstName(userInfo.getFirstName());

        return repo.save(account);
    }

    private boolean emailExists(String email) {
        User user = repo.findByEmail(email);
        if(user != null) {
            return true;
        }
        return false;
    }

    private String hashGenerator(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }
}
