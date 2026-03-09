package io.moji.authservice.model;

import lombok.Data;

@Data
public class LoginResponse {
  private final String token;
}
