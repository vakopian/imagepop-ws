package com.imagepop.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by nyanaga on 4/4/16.
 */
@Component
public class CurrentUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    public org.springframework.security.core.userdetails.User loadUserByUsername(String s) throws UsernameNotFoundException {
        User dbUser = repository.findByEmail(s);
        if (dbUser == null) {
            throw new UsernameNotFoundException("could not find user " + s);
        }
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        if (dbUser.getRoles() != null) {
            for (User.Role role: dbUser.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(role.toString()));
            }
        }

        return new CurrentUser(dbUser.getEmail(), dbUser.getPassword(), authorities);
    }
}