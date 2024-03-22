package com.example.untoldpsproject.repositories;

import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
}
