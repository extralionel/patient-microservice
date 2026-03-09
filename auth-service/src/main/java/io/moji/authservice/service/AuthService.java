package io.moji.authservice.service;

import io.jsonwebtoken.JwtException;
import io.moji.authservice.entity.User;
import io.moji.authservice.model.LoginRequest;
import io.moji.authservice.repository.UserRepository;
import io.moji.authservice.util.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthService(JwtUtil jwtUtil, UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.jwtUtil = jwtUtil;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public Optional<String> authenticate(LoginRequest request) {
    return userRepository.findByEmail(request.getEmail())
        .filter(u -> passwordEncoder.matches(request.getPassword(), u.getPassword()))
        .map(u -> jwtUtil.generateToken(u.getEmail(), u.getRole()));
  }

  public boolean validateToken(String token) {
    try {
      jwtUtil.validateToken(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }
}
