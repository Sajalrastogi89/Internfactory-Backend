package com.OurInternfactory.Security;

import com.OurInternfactory.Exceptions.ResourceNotFoundException;
import com.OurInternfactory.Repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Loading user from database by username
        return this.userRepo.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User ", "email : "+username, 0));
    }
}
