package com.imagepop.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;

/**
 * Created by nyanaga on 3/29/16.
 */
public class CurrentUserDetailService implements UserDetailsService{
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User dbUser = repository.findByEmail(s);
        HashSet<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (User.Role role: dbUser.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.toString()));
        }
        CurrentUser cu = new CurrentUser(dbUser.getPassword(), dbUser.getPassword(), authorities);

        return cu;
    }
}
