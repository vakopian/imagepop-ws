package com.imagepop.domain;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    public User registerNewUser(User userInfo) throws EmailExistsException { //Figure out the DTO stuff
        if (emailExists(userInfo.getEmail())) {
            throw new EmailExistsException("There is an account with the email address " + userInfo.getEmail());
        }
        userInfo.setPassword(hashGenerator(userInfo.getPassword()));
        repo.save(userInfo);

        return userInfo;
    }

    public User loginUser(User userInfo){
        User storedUser = null;
        if (userInfo != null)
            storedUser = repo.findByEmail(userInfo.getEmail());
        if (storedUser != null){
            String original = userInfo.getPassword();
            String hashed = storedUser.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (!passwordEncoder.matches(original, hashed))
                throw new BadCredentialsException("Incorrect password");
        }
        else
            throw new UsernameNotFoundException("User doesn't exist");
        return storedUser;
    }

    private boolean emailExists(String email) {
        User user = repo.findByEmail(email);
        if (user != null) {
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
