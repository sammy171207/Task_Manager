package com.example.taskmanagement.config;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.taskmanagement.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtTokenValidator extends OncePerRequestFilter {



    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // final String authorizationHeader = request.getHeader(JwtConstants.HEADER_STRING);

        // String username = null;
        // String jwt = null;

        // if (authorizationHeader != null && authorizationHeader.startsWith(JwtConstants.TOKEN_PREFIX)) {
        //     jwt = authorizationHeader.substring(7);
        //     username = jwtProvider.extractUsername(jwt);
        // }

        // if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

        //     UserDetails userDetails = userService.loadUserByUsername(username);

        //     if (jwtProvider.validateToken(jwt, userDetails.getUsername())) {

        //         UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        //                 new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        //         usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        //         SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        //     }
        // }
        // filterChain.doFilter(request, response);

        String jwt=request.getHeader(JwtConstants.HEADER_STRING);
        if(jwt!=null){
                 jwt=jwt.substring(7);
                 try {
                    SecretKey key =Keys.hmacShaKeyFor(JwtConstants.SECRET.getBytes());
                    Claims claims=Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
                    String username=String.valueOf(claims.get("username"));
                    String authorities=String.valueOf(claims.get("authorities"));

                    List<GrantedAuthority> auth=AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                    Authentication authentication=new UsernamePasswordAuthenticationToken(username,null, auth);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                 } catch (Exception e) {
                    throw new BadCredentialsException("invalid token");
                 }
    }
    filterChain.doFilter(request,response);
    }
}


