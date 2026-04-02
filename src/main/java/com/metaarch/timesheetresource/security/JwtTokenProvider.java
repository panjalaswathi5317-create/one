package com.metaarch.timesheetresource.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
  private final Key signingKey;

  public JwtTokenProvider(JwtProperties properties) {
    byte[] secretBytes = Objects.requireNonNull(properties.getSecret(), "JWT secret is required")
      .getBytes(StandardCharsets.UTF_8);
    this.signingKey = Keys.hmacShaKeyFor(secretBytes);
  }

  public boolean validateToken(String token) {
    parseClaims(token);
    return true;
  }

  public String getUsername(String token) {
    return parseClaims(token).getSubject();
  }

  public List<String> getRoles(String token) {
    Object roles = parseClaims(token).get("roles");
    if (roles instanceof List<?> list) {
      return list.stream().filter(Objects::nonNull).map(Object::toString).toList();
    }
    return Collections.emptyList();
  }

  private Claims parseClaims(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(signingKey)
      .build()
      .parseClaimsJws(token)
      .getBody();
  }
}
