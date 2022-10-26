package com.Internfactory.Services;

import com.Internfactory.model.CustomUserDetail;
import com.Internfactory.Repository.UserRepository;
import com.Internfactory.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.getById(username);
        if(user==null) {
            throw new UsernameNotFoundException("NO USER");
        }
        return new CustomUserDetail(user);
    }
}
