package com.numble.numdeal.layer.repository;

import com.numble.numdeal.layer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String id);
}
