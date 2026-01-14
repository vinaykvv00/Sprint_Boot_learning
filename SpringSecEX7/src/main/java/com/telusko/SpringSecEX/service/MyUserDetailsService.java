package com.telusko.SpringSecEX.service;

import com.telusko.SpringSecEX.model.UserPrinciple;
import com.telusko.SpringSecEX.model.Users;
import com.telusko.SpringSecEX.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub

        Users user = repo.findByUsername(username);
        if (user == null) {
            System.out.println("User not found " + username);
            throw new UsernameNotFoundException("User not found " + username);
        }
        return new UserPrinciple(user);

    }

}
