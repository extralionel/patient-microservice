package io.moji.authservice.controller;

import io.moji.authservice.model.LoginRequest;
import io.moji.authservice.model.LoginResponse;
import io.moji.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @Operation(summary = "Generate token on user login")
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    return authService.authenticate(request)
        .map(s -> ResponseEntity.ok(new LoginResponse(s)))
        .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

  }

  @Operation(summary = "Validates the token")
  @GetMapping("/validate")
  public ResponseEntity<Void> validate(@RequestHeader("Authorization") String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    return authService.validateToken(authHeader.substring(7)) ?
        ResponseEntity.ok().build() :
        ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

}
