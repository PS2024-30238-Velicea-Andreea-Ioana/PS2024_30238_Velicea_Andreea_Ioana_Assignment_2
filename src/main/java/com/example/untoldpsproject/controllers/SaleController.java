package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.constants.SaleConstants;
import com.example.untoldpsproject.dtos.*;
import com.example.untoldpsproject.entities.Sale;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.mappers.CategoryMapper;
import com.example.untoldpsproject.mappers.TicketMapper;
import com.example.untoldpsproject.services.SaleService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@AllArgsConstructor
@Setter
@Getter
@RequestMapping(value = "/sale")
public class SaleController {
    private final SaleService saleService;
    @GetMapping("/list/{userId}")
    public ModelAndView salesList(@PathVariable("userId")String userId) {
        ModelAndView mav = new ModelAndView("sale-list");
        List<SaleDto> sales = saleService.findSales();
        List<SaleDto> salesWithTickets = sales.stream()
                .filter(sale -> sale.getTickets() != null && !sale.getTickets().isEmpty())
                .collect(Collectors.toList());
        mav.addObject("sales", salesWithTickets);
        mav.addObject("userId", userId);
        return mav;
    }
    @GetMapping("/add/{userId}")
    public ModelAndView addSaleForm(@PathVariable("userId") String userId) {
        ModelAndView mav = new ModelAndView("sale-add");
        mav.addObject("saleDto", new SaleDto());
        List<Ticket> tickets = saleService.getTickets();
        mav.addObject("tickets", tickets);
        mav.addObject("userId", userId);
        return mav;
    }
    @PostMapping("/add/{userId}")
    public ModelAndView addSale(@PathVariable("userId") String userId,@ModelAttribute("saleDto") SaleDtoIds saleDtoIds, RedirectAttributes redirectAttributes) {
        List<Ticket> tickets = new ArrayList<>();
        if(saleDtoIds.getTickets()!=null){
            for(String ticketId : saleDtoIds.getTickets() ){
                Ticket ticket = saleService.findTicketById(ticketId);
                tickets.add(ticket);
            }
        }
        SaleDto saleDto = new SaleDto();
        saleDto.setDiscountPercentage(saleDtoIds.getDiscountPercentage());
        saleDto.setTickets(tickets);
        String result = saleService.insert(saleDto);
        if(result.equals(SaleConstants.SALE_INSERTED)){
            return new ModelAndView("redirect:/sale/list/"+userId);
        }else{
            redirectAttributes.addFlashAttribute("error", result);
            return new ModelAndView("redirect:/sale/add/"+userId);
        }
    }
    @GetMapping("/edit/{id}/{userId}")
    public ModelAndView editSaleForm(@PathVariable("userId") String userId, @PathVariable("id") String saleId){
        ModelAndView mav = new ModelAndView("sale-edit");
        SaleDto saleDto = saleService.findSaleById(saleId);
        mav.addObject("saleDto", saleDto);
        List<Ticket> tickets = saleService.getTickets();
        mav.addObject("tickets", tickets);
        mav.addObject("userId", userId);
        return mav;
    }
    @PostMapping("/edit/{id}/{userId}")
    public ModelAndView updateSake(@PathVariable("userId") String userId, @ModelAttribute("saleDto") SaleDto saleDto, RedirectAttributes redirectAttributes) {
        String result = saleService.updateSaleById(saleDto.getId(), saleDto);
        if(result.equals(SaleConstants.SALE_UPDATED)){
            return new ModelAndView("redirect:/sale/list/"+userId);
        }else{
            redirectAttributes.addFlashAttribute("error", result);
            return new ModelAndView("redirect:/sale/edit/"+saleDto.getId()+"/"+userId);
        }

    }

    @GetMapping("/delete/{id}/{userId}")
    public ModelAndView deleteSale(@PathVariable("userId") String userId, @PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        String result = saleService.deleteSaleById(id);
        redirectAttributes.addFlashAttribute("error",result);
        return new ModelAndView("redirect:/sale/list/"+userId);
    }
}
