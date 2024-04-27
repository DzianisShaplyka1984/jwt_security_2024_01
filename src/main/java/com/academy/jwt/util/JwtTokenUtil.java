package com.academy.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
        .signWith(SignatureAlgorithm.HS512, secret)
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
    Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

    return resolver.apply(claims);
  }
}
