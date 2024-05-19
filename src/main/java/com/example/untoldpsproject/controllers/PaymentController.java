package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.constants.PaymentConstants;
import com.example.untoldpsproject.dtos.CategoryDto;
import com.example.untoldpsproject.dtos.PaymentDto;
import com.example.untoldpsproject.services.PaymentService;
import com.example.untoldpsproject.strategies.TXTFileStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Controller class for managing payment operations.
 */
@RestController
@CrossOrigin
@AllArgsConstructor
@Setter
@Getter
@RequestMapping(value = "/payment")
public class PaymentController {
    private final PaymentService paymentService;

    /**
     * Displays the payment form.
     *
     * @param orderId The ID of the order.
     * @return A ModelAndView object for displaying the payment form.
     */
    @GetMapping("/add/{orderId}")
    public ModelAndView addPaymentForm(@PathVariable("orderId") String orderId) {
        ModelAndView mav = new ModelAndView("payment");
        mav.addObject("paymentDto", new PaymentDto());
        return mav;
    }

    /**
     * Handles the submission of the payment form.
     *
     * @param paymentDto         The payment DTO containing payment information.
     * @param orderId            The ID of the order.
     * @param redirectAttributes The redirect attributes.
     * @return A ModelAndView object for redirecting to the appropriate page after payment submission.
     */
    @PostMapping("/add/{orderId}")
    public ModelAndView addPayment(@ModelAttribute PaymentDto paymentDto,@PathVariable("orderId") String orderId, RedirectAttributes redirectAttributes) {
        String result = paymentService.insert(paymentDto);
        if (result.equals(PaymentConstants.PAYMENT_INSERTED)) {
            paymentService.actualizeOrderStatus(orderId);
            return new ModelAndView("redirect:/order/visualizeOrder/" + orderId);
        } else {
            redirectAttributes.addFlashAttribute("error", result); // Here, add result directly as error message
            return new ModelAndView("redirect:/payment/add/" + orderId);
        }
    }

    /**
     * Displays the payment method selection page.
     *
     * @param orderId The ID of the order.
     * @return A ModelAndView object for displaying the payment method selection page.
     */
    @GetMapping("/select-method/{orderId}")
    public ModelAndView selectPaymentMethod(@PathVariable("orderId") String orderId) {
        ModelAndView mav = new ModelAndView("payment-selection");
        mav.addObject("orderId", orderId);
        return mav;
    }

    /**
     * Processes the selected payment method.
     *
     * @param orderId       The ID of the order.
     * @param paymentMethod The selected payment method.
     * @return A ModelAndView object for redirecting to the appropriate page after payment processing.
     */
    @PostMapping("/process-payment")
    public ModelAndView processPayment(@RequestParam("orderId") String orderId, @RequestParam("paymentMethod") String paymentMethod) {
        if ("cash".equals(paymentMethod)) {
            return new ModelAndView("redirect:/order/visualizeOrder/" + orderId);
        }
        if ("card".equals(paymentMethod)) {
            return new ModelAndView("redirect:/payment/add/" + orderId);
        }
        return new ModelAndView("redirect:/error");
    }
}
