package com.example.untoldpsproject.repositories;

import com.example.untoldpsproject.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale,String> {
}
