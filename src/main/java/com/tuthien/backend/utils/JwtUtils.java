package com.tuthien.backend.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    @Value("${spring.jwt.secret}")
    private String jwtSecret;

    @Value("${spring.jwt.expire}")
    private Long expire;

    public String getUsernameFromToken(String token) {
        Claims claims = this.getClaims(token);
        return claims.getSubject();
    }

    public boolean isExpiredToken(String jwt) {
        try {
            Claims claims = this.getClaims(jwt);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException ex) {
            return true;
        }
    }

    private Claims getClaims(String jwt) {
        return Jwts
                .parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(this.jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + this.expire))
                .setSubject(username)
                .signWith(Keys.hmacShaKeyFor(this.jwtSecret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
}
