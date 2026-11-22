package com.example.tp3.repositories;

import com.example.tp3.models.Order;
import com.example.tp3.models.Product;
import com.example.tp3.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}

