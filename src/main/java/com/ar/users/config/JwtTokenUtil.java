package com.ar.users.config;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public String getToken(UUID id) {
        byte[] apiKeySecretBytes = secret.getBytes();
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("some_claim", "some_value");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        Date expirationDate = cal.getTime();
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(new Date())
                .setClaims(claims)
                .setSubject(id.toString())
                .setExpiration(expirationDate)
                .signWith(signatureAlgorithm, signingKey);
        return builder.compact();
    }

}
