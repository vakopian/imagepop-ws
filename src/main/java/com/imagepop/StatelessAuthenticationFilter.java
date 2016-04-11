package com.imagepop;

import org.jose4j.json.JsonUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class StatelessAuthenticationFilter extends RequestHeaderAuthenticationFilter {
    public static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
    private TokenHandler tokenHandler;

    public StatelessAuthenticationFilter() {
        setPrincipalRequestHeader(AUTH_HEADER_NAME);
        setExceptionIfHeaderMissing(false);
    }

    public void setTokenHandler(TokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String token = (String) super.getPreAuthenticatedPrincipal(request);
        if (token == null) {
            return null;
        }
        return tokenHandler.parseUserFromToken(token);
    }

    public void addAuthenticationInfoToResponse(HttpServletResponse response, UserDetails user) {
        String token = tokenHandler.createTokenForUser(user);
        response.addHeader(AUTH_HEADER_NAME, token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.addHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0, must-revalidate");
        response.addHeader(HttpHeaders.PRAGMA, "no-cache");
        response.addHeader(HttpHeaders.EXPIRES, "0");
        Map<String, Object> res = new HashMap<>();
        res.put("token", token);
        try (PrintWriter writer = response.getWriter()) {
            JsonUtil.writeJson(res, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}