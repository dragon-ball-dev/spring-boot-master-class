package com.springmasterclass.study.repository.auth;

import com.springmasterclass.study.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
}
