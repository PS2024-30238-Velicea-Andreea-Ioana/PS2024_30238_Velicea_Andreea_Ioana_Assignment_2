package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.constants.CartConstants;
import com.example.untoldpsproject.dtos.*;
import com.example.untoldpsproject.entities.*;
import com.example.untoldpsproject.mappers.*;
import com.example.untoldpsproject.services.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for managing cart operations.
 */
@RestController
@CrossOrigin
@AllArgsConstructor
@Setter
@Getter
@RequestMapping(value = "/cart")
public class CartController {
    private final CartService cartService;

    /**
     * Adds a ticket to the cart.
     *
     * @param id      The ID of the ticket to be added.
     * @param userId  The ID of the user.
     * @param cartId  The ID of the cart.
     * @return A ModelAndView object containing a redirection URL.
     */
    @GetMapping("/customer/tickets/{userId}/addToCart/{ticketId}/{cartId}")
    public ModelAndView addTicketToCartForm(@PathVariable("ticketId") String id, @PathVariable("userId") String userId, @PathVariable("cartId") String cartId,  RedirectAttributes redirectAttributes) {
        Ticket ticket = cartService.findTicket(id);
        User user = cartService.findUser(userId);
        Cart cartDto = cartService.findCartById(cartId);
        ModelAndView mav = new ModelAndView();
        if (ticket != null && ticket.getAvailable() > 0) {
            if (cartDto == null) {
                cartDto = new Cart();
                cartDto.setUser(user);
                cartDto.setCartItems(new ArrayList<>());
                cartDto.setTotalPrice(0.0);
                cartService.insert(CartMapper.toCartDto(cartDto));
            }
            CartItem existingCartItem = cartService.findCartItemByTicketIdAndCartId(id, cartId);
            if (existingCartItem == null) {
                CartItemDto newCartItemDto = new CartItemDto();
                newCartItemDto.setTicket(ticket);
                newCartItemDto.setQuantity(1.0);
                newCartItemDto.setCart(cartDto);
                cartService.insertCartItem(newCartItemDto);
            } else {
                if (ticket.getAvailable() > existingCartItem.getQuantity()) {
                    existingCartItem.setQuantity(existingCartItem.getQuantity() + 1.0);
                    cartService.updateCartItem(CartItemMapper.toCartItemDto(existingCartItem));
                }else{
                    redirectAttributes.addFlashAttribute("error", CartConstants.INVALID_QUANTITY);
                    mav.setViewName("redirect:/customer/seetickets/" + userId);
                    return mav;
                }
            }
            cartService.updateTotalPrice(cartId);
            mav = new ModelAndView("redirect:/cart/visualizeCart/" + cartId);
        } else {
            mav = new ModelAndView("redirect:/customer/seetickets/" + userId);
        }
        return mav;
    }

    /**
     * Adds a ticket to the cart.
     *
     * @param id      The ID of the ticket to be added.
     * @param userId  The ID of the user.
     * @param cartId  The ID of the cart.
     * @return A ModelAndView object containing a redirection URL.
     */
    @PostMapping("/customer/tickets/{userId}/addToCart/{ticketId}/{cartId}")
    public ModelAndView addTicketToCart(@PathVariable("ticketId") String id, @PathVariable("userId") String userId, @PathVariable("cartId") String cartId,  RedirectAttributes redirectAttributes) {
        return addTicketToCartForm(id, userId, cartId, redirectAttributes);
    }

    /**
     * Visualizes the content of the cart.
     *
     * @param cartId The ID of the cart.
     * @return A ModelAndView object containing the view name and the cart details.
     */
    @GetMapping("/visualizeCart/{cartId}")
    public ModelAndView visualizeCart(@PathVariable("cartId") String cartId) {
        ModelAndView mav = new ModelAndView("cart");
        Cart cart = cartService.findCartById(cartId);
        cartService.updateTotalPrice(cart.getId());
        List<CartItem> cartItems = cart.getCartItems();
        String userId = cart.getUser().getId();
        mav.addObject("cart", cart);
        mav.addObject("cartItems", cartItems);
        mav.addObject("userId", userId);
        mav.addObject("isAnyItemSoldOut", cartService.isAnyItemSoldOut(cart.getCartItems()));

        return mav;
    }



    /**
     * Decrease the quantity of an item from the cart.
     *
     * @param cartItemId The ID of the cart item to be descreased.
     * @return A ModelAndView object containing a redirection URL.
     */
    @GetMapping("/decreaseQuantity/{cartItemId}")
    public ModelAndView decreaseCartItem(@PathVariable("cartItemId") String cartItemId) {
        String cartId = cartService.decreaseCartItem(cartItemId);
        return new ModelAndView("redirect:/cart/visualizeCart/" + cartId);
    }

    /**
     * Increases the quantity of an item in the cart.
     *
     * @param cartItemId         The ID of the cart item.
     * @param redirectAttributes RedirectAttributes for handling flash attributes.
     * @return A ModelAndView object containing a redirection URL.
     */
    @GetMapping("/increaseQuantity/{cartItemId}")
    public ModelAndView increaseCartItem(@PathVariable("cartItemId") String cartItemId,RedirectAttributes redirectAttributes) {
        CartItem cartItem = cartService.getCartItemById(cartItemId);
        Double initialQuantity = cartItem.getQuantity();
        String cartId = cartService.increaseCartItem(cartItemId);
        Double modifiedQuantity =  cartItem.getQuantity();
        if(initialQuantity.equals(modifiedQuantity - 1)){
            return new ModelAndView("redirect:/cart/visualizeCart/" + cartId);
        }else{
            ModelAndView mav = new ModelAndView("redirect:/cart/visualizeCart/" + cartId);
            redirectAttributes.addFlashAttribute("error", CartConstants.INVALID_QUANTITY);
            return mav;
        }
    }

    /**
     * Removes an item from the cart.
     *
     * @param cartItemId The ID of the cart item to be removed.
     * @return A ModelAndView object containing a redirection URL.
     */
    @GetMapping("/removeItem/{cartItemId}")
    public ModelAndView removeItemFromCart(@PathVariable("cartItemId") String cartItemId){
        String cartId = cartService.removeCartItem(cartItemId);
        return new ModelAndView("redirect:/cart/visualizeCart/" + cartId);
    }
}
