package com.imagepop;

import com.imagepop.domain.CurrentUserDetailService;
import org.jose4j.lang.JoseException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenAuthenticationService {

    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";

    private final TokenHandler tokenHandler;

    public TokenAuthenticationService(String secret, CurrentUserDetailService userService) throws JoseException {
        tokenHandler = new TokenHandler(secret, userService);
    }

    public void addAuthentication(HttpServletResponse response, UserAuthentication authentication) {
        final User user = authentication.getDetails();
        try {
            response.addHeader(AUTH_HEADER_NAME, tokenHandler.createTokenForUser(user));
        } catch (JoseException e) {
            System.out.println("Token could not be created: " +e);
        }
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (token != null) {
            final User user = tokenHandler.parseUserFromToken(token);
            if (user != null) {
                return new UserAuthentication(user);
            }
        }
        return null;
    }
}
