package com.numble.numdeal.layer.repository;

import com.numble.numdeal.layer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String id);
    Optional<User> findByEmail(String id);
}
