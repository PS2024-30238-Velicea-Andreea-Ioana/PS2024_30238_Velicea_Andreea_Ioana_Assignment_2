package com.example.untoldpsproject.repositories;

import com.example.untoldpsproject.entities.Category;
import com.example.untoldpsproject.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
