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

@RestController
@CrossOrigin
@AllArgsConstructor
@Setter
@Getter
@RequestMapping(value = "/payment")
public class PaymentController {
    private final PaymentService paymentService;
    @GetMapping("/add/{orderId}")
    public ModelAndView addPaymentForm(@PathVariable("orderId") String orderId) {
        ModelAndView mav = new ModelAndView("payment");
        mav.addObject("paymentDto", new PaymentDto());
        return mav;
    }
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
    @GetMapping("/select-method/{orderId}")
    public ModelAndView selectPaymentMethod(@PathVariable("orderId") String orderId) {
        ModelAndView mav = new ModelAndView("payment-selection");
        mav.addObject("orderId", orderId);
        return mav;
    }
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
