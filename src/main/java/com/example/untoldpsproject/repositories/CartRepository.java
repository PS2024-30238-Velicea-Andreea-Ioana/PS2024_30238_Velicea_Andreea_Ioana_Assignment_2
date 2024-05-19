package com.example.untoldpsproject.repositories;

import com.example.untoldpsproject.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,String> {
    Cart findCartByUserId(String userId);
}
