package com.academy.jwt.filter;

import com.academy.jwt.util.JwtTokenUtil;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_SCHEMA = "Bearer ";
  public static final int BEARER_INDEX = 7;

  private final JwtTokenUtil jwtTokenUtil;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String requestHeader = request.getHeader(AUTHORIZATION_HEADER);

    String token = null;
    String name = null;

    if (requestHeader != null && requestHeader.startsWith(BEARER_SCHEMA)) {
      token = requestHeader.substring(BEARER_INDEX);

      name = jwtTokenUtil.getUsername(token);
    } else {
      logger.error("Auth header contains invalid format");
    }

    if (name != null && jwtTokenUtil.validateToken(token)) {
      UserDetails user = userDetailsService.loadUserByUsername(name);

      AbstractAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

      authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

      SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    filterChain.doFilter(request, response);
  }
}