package com.example.tutorialwebapp.loginDB;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Long> {
    public User findByEmail(String email);
    public boolean existsByEmail(String email);
}
