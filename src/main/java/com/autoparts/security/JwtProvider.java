package com.autoparts.security;

import com.autoparts.config.AppProperties;
import com.autoparts.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtProvider {

    private final AppProperties properties;

    public JwtProvider(AppProperties properties) {
        this.properties = properties;
    }

    public String generateToken(User user) {
        var iat = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        var exp = Date.from(LocalDate.now().plusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant());
        var jwtPayload = new JwtPayload(
                user.getId().toString(), user.getUsername(), user.getEmail(),
                user.getRole().getAuthority(),
                UUID.randomUUID().toString(), iat, exp);
        var mapper = new ObjectMapper();
        var payload = "";
        try {
            payload = mapper.writeValueAsString(jwtPayload);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Jwts.builder()
                .setPayload(payload)
                .signWith(SignatureAlgorithm.HS512, properties.getJwtSecret())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(properties.getJwtSecret()).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.warn("Token expired");
        } catch (UnsupportedJwtException unsEx) {
            log.warn("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            log.warn("Malformed jwt");
        } catch (SignatureException sEx) {
            log.warn("Invalid signature");
        } catch (Exception e) {
            log.warn("invalid token");
        }
        return false;
    }

    public String getLoginFromToken(String token) {
        var claims = Jwts.parser().setSigningKey(properties.getJwtSecret()).parseClaimsJws(token).getBody();
        return (String) claims.get("username");
    }
}
