package com.academy.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

  private final long EXPIRATION_TIME = 1000000 * 30;

  @Value("${jwt.secret}")
  private String secret;

  public String generateToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration((new Date(System.currentTimeMillis() + EXPIRATION_TIME)))
        .signWith(getSignInKey(), SignatureAlgorithm.HS512)
        .compact();
  }

  public boolean validateToken(String token) {
    return !isExpired(token);
  }

  private boolean isExpired(String token) {
    Date expirationDate = getClaim(token, Claims::getExpiration);

    return expirationDate.before(new Date());
  }

  public String getUsername(String token) {
    return getClaim(token, Claims::getSubject);
  }

  private <T> T getClaim(String token, Function<Claims, T> resolver) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();

    return resolver.apply(claims);
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
