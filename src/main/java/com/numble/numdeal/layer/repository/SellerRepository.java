package com.numble.numdeal.layer.repository;

import com.numble.numdeal.layer.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    boolean existsByEmail(String id);
}
