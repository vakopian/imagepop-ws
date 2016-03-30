package com.imagepop.domain;

/**
 * Created by BernardXie on 3/21/16.
 */
public interface UserService {
    User registerNewUser(User user)
            throws EmailExistsException;
}
