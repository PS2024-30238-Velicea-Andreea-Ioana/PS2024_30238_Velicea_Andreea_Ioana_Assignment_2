package com.example.untoldpsproject;

import com.example.untoldpsproject.constants.PaymentConstants;
import com.example.untoldpsproject.controllers.PaymentController;
import com.example.untoldpsproject.dtos.PaymentDto;
import com.example.untoldpsproject.services.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {UntoldPsProjectApplication.class})
public class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @Test
    public void testAddPayment() {
        String orderId = "123";
        PaymentDto paymentDto = new PaymentDto();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(paymentService.insert(any(PaymentDto.class))).thenReturn(PaymentConstants.PAYMENT_INSERTED);
        ModelAndView modelAndView = paymentController.addPayment(paymentDto, orderId, redirectAttributes);
        verify(paymentService, times(1)).insert(paymentDto);
        verify(paymentService, times(1)).actualizeOrderStatus(orderId);
        assertEquals("redirect:/order/visualizeOrder/" + orderId, modelAndView.getViewName());
    }

    @Test
    public void testAddPayment_Error() {
        String orderId = "123";
        PaymentDto paymentDto = new PaymentDto();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        String errorMessage = "Error Message";
        when(paymentService.insert(any(PaymentDto.class))).thenReturn(errorMessage);
        ModelAndView modelAndView = paymentController.addPayment(paymentDto, orderId, redirectAttributes);
        verify(paymentService, times(1)).insert(paymentDto);
        verify(redirectAttributes, times(1)).addFlashAttribute("error", errorMessage);
        assertEquals("redirect:/payment/add/" + orderId, modelAndView.getViewName());
    }

    @Test
    public void testSelectPaymentMethod() {
        String orderId = "123";
        ModelAndView expectedModelAndView = new ModelAndView("payment-selection");
        expectedModelAndView.addObject("orderId", orderId);
        ModelAndView modelAndView = paymentController.selectPaymentMethod(orderId);
        assertEquals(expectedModelAndView.getViewName(), modelAndView.getViewName());
        assertEquals(expectedModelAndView.getModel().get("orderId"), modelAndView.getModel().get("orderId"));
    }

    @Test
    public void testProcessPayment_Cash() {
        String orderId = "123";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("paymentMethod", "cash");
        ModelAndView modelAndView = paymentController.processPayment(orderId, "cash");
        assertEquals("redirect:/order/visualizeOrder/" + orderId, modelAndView.getViewName());
    }

    @Test
    public void testProcessPayment_Card() {
        String orderId = "123";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("paymentMethod", "card");
        ModelAndView modelAndView = paymentController.processPayment(orderId, "card");
        assertEquals("redirect:/payment/add/" + orderId, modelAndView.getViewName());
    }
}
