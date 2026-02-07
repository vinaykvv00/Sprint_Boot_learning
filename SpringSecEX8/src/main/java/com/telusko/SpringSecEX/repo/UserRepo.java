package com.telusko.SpringSecEX.repo;

import com.telusko.SpringSecEX.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Users, Integer> {

    Users findByUsername(String username);

}
