package com.telusko.SpringSecEX.service;

import com.telusko.SpringSecEX.model.Users;
import com.telusko.SpringSecEX.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Users register (@RequestBody Users user)
    {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
}
