package com.autoparts.security;

import lombok.Value;

@Value
public class AuthRequest {
    String username;
    String password;
}
