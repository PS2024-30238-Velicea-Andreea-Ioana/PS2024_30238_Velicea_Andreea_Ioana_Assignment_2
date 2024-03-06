package com.example.untoldpsproject.repositories;

import com.example.untoldpsproject.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
