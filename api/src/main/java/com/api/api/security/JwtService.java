package com.api.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.crypto.SecretKey;

@Service
public class JwtService {

  private static final String SECRET = "cambia-esta-clave-segura-de-32-bytes-o-mas-porfa!";
  private static final long EXP_MS = 1000L * 60 * 60 * 4; // 4h

  private SecretKey key() {
    return Keys.hmacShaKeyFor(SECRET.getBytes());
  }

  public String generate(String username, List<String> roles) {
    Date now = new Date();
    Map<String, Object> claims = new HashMap<>();
    claims.put("roles", roles);

    return Jwts.builder()
        .setSubject(username)
        .addClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + EXP_MS))
        .signWith(key())
        .compact();
  }

  public Claims validate(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public String getUsername(String token) {
    return validate(token).getSubject();
  }

  @SuppressWarnings("unchecked")
  public List<String> getRoles(String token) {
    return (List<String>) validate(token).get("roles");
  }
}
