package com.imagepop.domain;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by nyanaga on 4/4/16.
 */
public class CurrentUser extends org.springframework.security.core.userdetails.User {

    public CurrentUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

}
