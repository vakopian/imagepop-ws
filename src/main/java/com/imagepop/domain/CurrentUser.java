package com.imagepop.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;

/**
 * Created by nyanaga on 3/29/16.
 */
public class CurrentUser extends User {

    public CurrentUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public CurrentUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
                       boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

}
