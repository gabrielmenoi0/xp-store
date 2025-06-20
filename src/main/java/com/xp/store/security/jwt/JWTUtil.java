package com.xp.store.security.jwt;

import com.xp.store.utils.Time.TimeUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.Date;
@Component
public class JWTUtil implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    public static final long JWT_TOKEN_VALIDITY = 2 * 60 * 60 * 1000;

//    @Value("${jwt.secret}")
//    private String secretKey;
    @Value("${jwt.secret}")
    private String secretKey = "ZmYyY2Y2MzA5MzM0NDJlYmJmYjYyNjM2NzJjZjY0NzA2ODc4NzEyYzM4ZjczY2E0MzhiOTZlZTVjY2YyYzA0NmY4Y2QzNjJmYmExZWYxMzQ1YjhkMjliY2Y3MzU4NDM2Y2I3YzQzZTA2YzMwN2JkZmM0";
    public Jws<Claims> decode(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    }

    public String createToken(String email) {
        Date expiredDate = new Date(TimeUtils.getCurrentDateInSaoPaulo().getTime() + JWT_TOKEN_VALIDITY);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String refreshToken(String token) {
        String email = getEmailFromJwtToken(token);
        return createToken(email);
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

}