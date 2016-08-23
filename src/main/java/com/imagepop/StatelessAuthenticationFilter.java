package com.imagepop;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StatelessAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {
    private static final String TOKEN_COOKIE_NAME = "access_token";
    private TokenHandler tokenHandler;
    private boolean exceptionIfCookieMissing = false;
    private boolean tokenCookieSecure = false;
    private int tokenCookieMaxAge = -1;

    public void setTokenHandler(TokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;
    }

    public void setExceptionIfCookieMissing(boolean exceptionIfCookieMissing) {
        this.exceptionIfCookieMissing = exceptionIfCookieMissing;
    }

    public void setTokenCookieSecure(boolean tokenCookieSecure) {
        this.tokenCookieSecure = tokenCookieSecure;
    }

    public void setTokenCookieMaxAge(int tokenCookieMaxAge) {
        this.tokenCookieMaxAge = tokenCookieMaxAge;
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String token = null;
        for (Cookie cookie : request.getCookies()) {
            if (TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }
        if (token == null) {
            if (exceptionIfCookieMissing) {
                throw new PreAuthenticatedCredentialsNotFoundException("could not find cookie named '" + TOKEN_COOKIE_NAME + "' in request.");
            }
            return null;
        }
        return tokenHandler.getAuthentication(token);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A";
    }

    public void addAuthenticationInfoToResponse(HttpServletResponse response, UserDetails user) {
        String token = tokenHandler.createToken(user);
        Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(tokenCookieSecure);
        cookie.setMaxAge(tokenCookieMaxAge);
        response.addCookie(cookie);
    }
}
