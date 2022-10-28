package com.OurInternfactory.Security;

import lombok.Data;
@Data
public class JwtAuthRequest {
    private String username;
    private String password;
}

