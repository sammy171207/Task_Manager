package com.example.taskmanagement.config;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
    private final SecretKey key = Keys.hmacShaKeyFor(JwtConstants.SECRET.getBytes());

    public String generateToken(Authentication auth) {
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String roles = populateAuthorities(authorities);

        String jwt = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + JwtConstants.EXPIRATION_TIME))
                .claim("email", auth.getName())
                .claim("authorities", roles)
                .signWith(key)
                .compact();

        return jwt;
    }
   public String getUsernameFromJwtToken(String jwt){
    jwt=jwt.substring(7);
    Claims claims=Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
    String username=String.valueOf(claims.get("username"));
    return username;
   }
    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
       Set<String> auth=new HashSet<>();
          for(GrantedAuthority authority:authorities){
              auth.add(authority.getAuthority());
          }
          return String.join(",",auth);
    }

    // public boolean validateToken(String jwt, String username) {
    //     String extractedUsername = extractUsername(jwt);
    //     return (extractedUsername.equals(username) && !isTokenExpired(jwt));
    // }

    // public String extractUsername(String jwt) {
    //     return Jwts.parser()
    //             .setSigningKey(key)
    //             .build()
    //             .parseClaimsJws(jwt)
    //             .getBody()
    //             .getSubject();
    // }

    // private boolean isTokenExpired(String token) {
    //     return Jwts.parser()
    //             .setSigningKey(key)
    //             .build()
    //             .parseClaimsJws(token)
    //             .getBody()
    //             .getExpiration()
    //             .before(new Date());
    // }
}
