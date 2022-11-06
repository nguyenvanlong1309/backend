package com.tuthien.backend.utils;

import com.tuthien.backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
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
        Claims claims = this.getClaims(jwt);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
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
