package com.example.untoldpsproject;

import com.example.untoldpsproject.constants.PaymentConstants;
import com.example.untoldpsproject.controllers.CartController;
import com.example.untoldpsproject.controllers.PaymentController;
import com.example.untoldpsproject.dtos.CartItemDto;
import com.example.untoldpsproject.dtos.PaymentDto;
import com.example.untoldpsproject.dtos.TicketDto;
import com.example.untoldpsproject.entities.*;
import com.example.untoldpsproject.mappers.CartMapper;
import com.example.untoldpsproject.mappers.CategoryMapper;
import com.example.untoldpsproject.mappers.TicketMapper;
import com.example.untoldpsproject.mappers.UserMapper;
import com.example.untoldpsproject.services.*;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
