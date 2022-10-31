package com.OurInternfactory.Payloads;

import org.springframework.stereotype.Component;

@Component
public class JwtAuthResponse {
    private String token;
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
