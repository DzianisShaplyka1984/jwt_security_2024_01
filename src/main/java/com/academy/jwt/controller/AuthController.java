package com.academy.jwt.controller;

import com.academy.jwt.dto.JwtRequestDto;
import com.academy.jwt.dto.JwtResponseDto;
import com.academy.jwt.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final JwtTokenUtil jwtTokenUtil;
  private final AuthenticationManager authenticationManager;

  @PostMapping("/auth")
  public JwtResponseDto auth(@RequestBody JwtRequestDto jwtRequestDto) {

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(jwtRequestDto.getUserName(),
            jwtRequestDto.getPassword()));

    var token = jwtTokenUtil.generateToken(jwtRequestDto.getUserName());

    return new JwtResponseDto(token);
  }
}
