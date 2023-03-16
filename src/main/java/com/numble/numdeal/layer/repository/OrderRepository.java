package com.numble.numdeal.layer.repository;

import com.numble.numdeal.layer.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
