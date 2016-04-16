package com.imagepop;

import org.jose4j.json.JsonUtil;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.jose4j.lang.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Map;

@Component
public final class TokenHandler {
    @Value("${app.authentication.restful.tokenValidityMinutes:1440}")
    protected int tokenValidityMinutes;
    @Value("${app.authentication.restful.jwt.secret:84447a04-0aa6-46c2-9846-2aa7a4ebc166-e6f0d044-9f0f-4a51-9e22-7d74b552d5cb}")
    private String secret;
    @Value("${app.authentication.restful.jwt.issuer:deepcontrast}")
    protected String issuer;
    @Value("${app.authentication.restful.jwt.audience:deepcontrast}")
    protected String audience;
    @Autowired
    protected UserDetailsService userService;

    public UserDetails parseUserFromToken(String token) {
        try {
            HmacKey key = getKey();
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime()
                    .setAllowedClockSkewInSeconds(30)
                    .setRequireSubject()
                    .setExpectedIssuer(issuer)
                    .setExpectedAudience(audience)
                    .setVerificationKey(key)
                    .build();
            JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
            String username = jwtClaims.getSubject();
            return userService.loadUserByUsername(username);
        } catch (InvalidJwtException | MalformedClaimException e) {
            throw new RuntimeException(e);
        }
    }

    public String createTokenForUser(UserDetails user) {
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(issuer);
        claims.setAudience(audience);
        claims.setExpirationTimeMinutesInTheFuture(tokenValidityMinutes);
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setNotBeforeMinutesInThePast(2);
        claims.setSubject(user.getUsername());

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        HmacKey key = getKey();
        jws.setKey(key);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA512);
        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException(e);
        }
    }

    private HmacKey getKey() {
        return new HmacKey(StringUtil.getBytes(secret, Charset.defaultCharset()));
    }
}
