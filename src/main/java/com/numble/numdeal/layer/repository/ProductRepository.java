package com.numble.numdeal.layer.repository;

import com.numble.numdeal.layer.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findByProductId(Long productId);
}