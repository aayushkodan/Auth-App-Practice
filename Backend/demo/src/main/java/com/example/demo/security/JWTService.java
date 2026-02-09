package com.example.demo.security;

import com.example.demo.entities.Role;
import com.example.demo.io.UserDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
@Data
public class JWTService {

    private final SecretKey key;
    private final String issuer;
    private final long accessTtlSeconds;
    private final long refreshTtlSeconds;

    public JWTService(
            @Value("${security.jwt.secret}") String key,
            @Value("${security.jwt.issuer}")String issuer,
            @Value("${security.jwt.access-ttl-seconds}")long accessTtlSeconds,
            @Value("${security.jwt.refresh-ttl-seconds}") long refreshTtlSeconds) {

        if(key == null && key.length()<64){
            throw new IllegalArgumentException("Invalid secret key");
        }

        this.key = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.accessTtlSeconds = accessTtlSeconds;
        this.refreshTtlSeconds = refreshTtlSeconds;
    }

    public String generateAccessToken(UserDTO user){
        Instant now = Instant.now();
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getId().toString())
                .issuer(issuer)
                .claims(Map.of(
                        "typ","access",
                        "email",user.getEmail(),
                        "roles",user.getRoles().stream().map(Role::getName).toList()
                ))
                .expiration(Date.from(now.plusSeconds(accessTtlSeconds)))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(UserDTO user,String jti){
        return Jwts.builder()
                .id(jti)
                .subject(user.getId().toString())
                .issuer(issuer)
                .claim("typ","refresh")
                .expiration(Date.from(Instant.now().plusSeconds(refreshTtlSeconds)))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Jws<Claims> parse(String token){
        try {
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
        } catch (JwtException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isAccessToken(String token){
        return parse(token).getPayload().get("typ").equals("access");
    }

    public boolean isRefreshToken(String token){
        return parse(token).getPayload().get("typ").equals("refresh");
    }

    public String getUserId(String token){
        return parse(token).getPayload().getSubject();
    }

    public String getJti(String token){
        return parse(token).getPayload().getId();
    }

}
