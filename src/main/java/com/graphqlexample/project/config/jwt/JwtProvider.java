package com.graphqlexample.project.config.jwt;

import io.jsonwebtoken.*;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.time.ZoneId;
import java.time.LocalDate;

@Log
@Component
public class JwtProvider {

  @Value("$(jwt.secret)")
  private String jwtSecret;

  public String generateToken(String usename) {
    Date date = Date.from(LocalDate.now().plusDays(15).
      atStartOfDay(ZoneId.systemDefault()).toInstant());
    return Jwts.builder()
      .setSubject(usename)
      .setExpiration(date)
      .signWith(SignatureAlgorithm.HS512, jwtSecret)
      .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
      return true;
    }  catch (ExpiredJwtException expEx) {
      log.severe("Token expired");
    } catch (UnsupportedJwtException unsEx) {
      log.severe("Unsupported jwt");
    } catch (MalformedJwtException mjEx) {
      log.severe("Malformed jwt");
    } catch (SignatureException sEx) {
      log.severe("Invalid signature");
    } catch (Exception e) {
      log.severe("invalid token");
    }
    return false;
  }

  public String getLoginFromToken(String token) {
    Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    return claims.getSubject();
  }
}
