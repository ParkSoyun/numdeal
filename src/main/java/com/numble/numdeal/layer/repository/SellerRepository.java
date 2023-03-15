package com.numble.numdeal.layer.repository;

import com.numble.numdeal.layer.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    boolean existsByEmail(String email);
    Optional<Seller> findByEmail(String email);
}
