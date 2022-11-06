package com.OurInternfactory.Services;

import com.OurInternfactory.Exceptions.Apiexception;
import com.OurInternfactory.Payloads.JwtAuthResponse;
import com.OurInternfactory.Security.JwtTokenHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class JWTTokenGenerator {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenHelper jwtTokenHelper;
    private final UserDetailsService userDetailsService;

    public JWTTokenGenerator(AuthenticationManager authenticationManager, JwtTokenHelper jwtTokenHelper, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenHelper = jwtTokenHelper;
        this.userDetailsService = userDetailsService;
    }

    private void authenticate(String usernane, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usernane, password);
        try{
            this.authenticationManager.authenticate(authenticationToken);
        }
        catch (BadCredentialsException e) {
            System.out.println("Invalid Details");
            throw new Apiexception("Invalid Username or Password");
        }
    }
    public JwtAuthResponse getTokenGenerate(String email, String Password){
        this.authenticate(email, Password);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
        this.jwtTokenHelper.generateToken(userDetails);
        String token = this.jwtTokenHelper.generateToken(userDetails);
        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token);
        return response;
    }
}
