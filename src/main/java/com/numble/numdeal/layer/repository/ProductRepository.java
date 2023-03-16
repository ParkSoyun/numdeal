package com.numble.numdeal.layer.repository;

import com.numble.numdeal.layer.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository {
}
