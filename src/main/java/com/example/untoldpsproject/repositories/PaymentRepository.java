package com.example.untoldpsproject.repositories;

import com.example.untoldpsproject.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,String> {
}
