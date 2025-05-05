package com.example.cab_booking.security.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.cab_booking.security.services.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    @Value("${app.jwtSecret}")
    private String jwtSecret;          // must be Base64 of ≥ 64 bytes for HS512

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Builds a JWT with:
     *  - subject = email
     *  - custom claims: id, role
     *  - issuedAt, expiration, HS512 signature
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        Date now       = new Date();
        Date expiresAt = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                   // ← use email here so your filter can loadByEmail
                  .setSubject(user.getEmail())
                  .claim("id",   user.getId())
                  .claim("role", user.getAuthorities().iterator().next().getAuthority())
                  .setIssuedAt(now)
                  .setExpiration(expiresAt)
                  .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                  .compact();
    }

    /**
     * Extracts the subject (now the email) from the JWT.
     */
    public String getEmailFromJwtToken(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(getSigningKey())
                   .build()
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject();
    }

    /**
     * Validates signature, expiration, etc.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            // you can log e.getMessage() here if you like
        }
        return false;
    }
}
