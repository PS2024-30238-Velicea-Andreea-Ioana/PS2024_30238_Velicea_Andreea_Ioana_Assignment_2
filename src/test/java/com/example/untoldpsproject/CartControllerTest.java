package com.example.untoldpsproject;

import com.example.untoldpsproject.controllers.CartController;
import com.example.untoldpsproject.services.CartService;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CartControllerTest {

    @Autowired
    private CartController cartController;

    @MockBean
    private CartService cartService;

    @Mock
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddTicketToCartForm() {
        String ticketId = "1";
        String quantity = "2";
        String userId = "user123";
        ModelAndView result = cartController.addTicketToCartForm(ticketId, quantity, userId, redirectAttributes);
    }

    @Test
    public void testAddTicketToCart() {
        String ticketId = "1";
        String quantity = "2";
        String userId = "user123";
        ModelAndView result = cartController.addTicketToCart(ticketId, quantity, userId, redirectAttributes);
    }
}