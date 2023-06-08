package com.autoparts.security;

import lombok.Value;

import java.util.Date;

@Value
public class JwtPayload {
    String id;
    String username;
    String email;
    String role;
    String jti;
    Date iat;
    Date exp;
}
