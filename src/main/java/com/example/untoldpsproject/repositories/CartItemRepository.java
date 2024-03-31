package com.example.untoldpsproject.repositories;

import com.example.untoldpsproject.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,String> {
    List<CartItem> findCartItemsByCartId(String cartId);
    CartItem findCartItemByTicketIdAndAndCartId(String ticketId, String cartId);
}
