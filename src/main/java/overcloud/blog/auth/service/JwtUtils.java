package overcloud.blog.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtils {
    private final Long validAccessTokenTime;
    private final Long validRefreshTokenSeconds;
    private final Key key;

    public JwtUtils(@Value("${blog.auth.token.sign-key}") String signKey,
                    @Value("${blog.auth.token.valid-access-time}") Long validAccessTokenTime,
                    @Value("${blog.auth.token.valid-refresh-time}") Long validRefreshTokenSeconds) {
        this.validAccessTokenTime = validAccessTokenTime;
        this.validRefreshTokenSeconds = validRefreshTokenSeconds;
        this.key = Keys.hmacShaKeyFor(signKey.getBytes(StandardCharsets.UTF_8));
    }

    public String encode(String sub) {
        if (sub == null || sub.equals("")) {
            return null;
        }

        Instant exp = Instant.now();
        return Jwts.builder().setSubject(sub)
                .setIssuedAt(new Date(exp.toEpochMilli()))
                .setExpiration(new Date(exp.toEpochMilli() + validAccessTokenTime * 1000))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String jwt) throws JwtException {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            Instant now = Instant.now();
            Date exp = claims.getExpiration();
            return exp.after(Date.from(now));
        } catch (JwtException ex) {
            return false;
        }
    }

    public String getSub(String jwt) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            return claims.getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    public String generateRefreshToken(String subject) {
        Instant exp = Instant.now();

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(exp.toEpochMilli() + validRefreshTokenSeconds * 1000))
                .signWith(key).compact();
    }

}
