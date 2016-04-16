package com.imagepop.domain;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by BernardXie on 3/21/16.
 */
public interface UserService {
    User registerNewUser(User user)
            throws EmailExistsException;
    User loginUser(User user) throws BadCredentialsException, UsernameNotFoundException;
}
