package com.imagepop;

import com.imagepop.domain.CurrentUserDetailService;

import org.jose4j.json.JsonUtil;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public final class TokenHandler {

    private final String secret;
    private final CurrentUserDetailService userService;
    private final RsaJsonWebKey rsaJsonWebKey;

    public TokenHandler(String secret, CurrentUserDetailService userService)
            throws JoseException {
        this.secret = secret;
        this.userService = userService;
        this.rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
    }

    public User parseUserFromToken(String token) {
        JsonWebSignature jws = new JsonWebSignature();
        try {
            jws.setCompactSerialization(token);
            if( !jws.verifySignature() )
            {
                throw new JoseException( "JSON Web Token signature verification failed" );
            }
            Map<String, Object> claims = JsonUtil.parseJson(jws.getPayload());
            return userService.loadUserByUsername((String) claims.get("username"));
        } catch (JoseException e) {
            System.out.println("Invalid token! " + e);
            return null;
        }
    }

    public String createTokenForUser(User user) throws JoseException {
        JwtClaims claims = new JwtClaims();
        claims.setIssuer("Issuer");
        claims.setAudience("Audience");
        claims.setExpirationTimeMinutesInTheFuture(10);
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setNotBeforeMinutesInThePast(2);
        claims.setSubject("subject");
        claims.setClaim("username", user.getUsername());

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(rsaJsonWebKey.getPrivateKey());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        return jws.getCompactSerialization();
    }
}
