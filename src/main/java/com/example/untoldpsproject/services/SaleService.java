package com.example.untoldpsproject.services;

import com.example.untoldpsproject.constants.SaleConstants;
import com.example.untoldpsproject.constants.TicketConstants;
import com.example.untoldpsproject.dtos.SaleDto;
import com.example.untoldpsproject.entities.Sale;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.mappers.SaleMapper;
import com.example.untoldpsproject.repositories.SaleRepository;
import com.example.untoldpsproject.repositories.TicketRepository;
import com.example.untoldpsproject.validators.SaleValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@Service
public class SaleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaleService.class);
    private final SaleRepository saleRepository;
    private final TicketRepository ticketRepository;
    private final SaleValidator saleValidator = new SaleValidator();

    public String insert(SaleDto saleDto){
        try{
            saleValidator.saleDtoValidator(saleDto);
            Sale sale = SaleMapper.toSale(saleDto);
            saleRepository.save(sale);
            List<Ticket> tickets = saleDto.getTickets();
            for(Ticket ticket: tickets){
                ticket.setSale(sale);
                ticket.setDiscountedPrice(addDiscount(saleDto.getDiscountPercentage(),ticket.getPrice()));
                ticketRepository.save(ticket);
            }
            LOGGER.debug(SaleConstants.SALE_INSERTED);
            return SaleConstants.SALE_INSERTED;
        }catch (Exception e){
            LOGGER.error(SaleConstants.SALE_NOT_INSERTED + " " + e.getMessage());
            return SaleConstants.SALE_NOT_INSERTED + " " + e.getMessage();
        }
    }

    public List<SaleDto> findSales(){
        List<Sale> saleList = saleRepository.findAll();
        return saleList.stream().map(SaleMapper::toSaleDto).collect(Collectors.toList());
    }

    public SaleDto findSaleById(String id){
        Optional<Sale> saleOptional = saleRepository.findById(id);
        if(saleOptional.isEmpty()){
            LOGGER.error(SaleConstants.SALE_NOT_FOUND);
            return null;
        }else {
            return SaleMapper.toSaleDto(saleOptional.get());
        }
    }

    public String updateSaleById(String id, SaleDto updatesSaleDto){
        Optional<Sale> saleOptional = saleRepository.findById(id);
        if(saleOptional.isEmpty()){
            LOGGER.error(SaleConstants.SALE_NOT_FOUND);
            return SaleConstants.SALE_NOT_FOUND;
        }else{
            Sale sale = saleOptional.get();
            try {
                saleValidator.saleDtoValidator(updatesSaleDto);
                Sale updatedSale = SaleMapper.toSale(updatesSaleDto);
                updatedSale.setDiscountPercentage(updatesSaleDto.getDiscountPercentage());
                updatedSale.setTickets(updatesSaleDto.getTickets());
                List<Ticket> tickets = updatesSaleDto.getTickets();
                for(Ticket ticket: tickets){
                    ticket.setSale(sale);
                    ticket.setDiscountedPrice(addDiscount(updatesSaleDto.getDiscountPercentage(),ticket.getPrice()));
                    ticketRepository.save(ticket);
                }
                saleRepository.save(sale);
                LOGGER.debug(SaleConstants.SALE_UPDATED);
                return SaleConstants.SALE_UPDATED;
            }catch (Exception e){
                LOGGER.error(SaleConstants.SALE_NOT_UPDATED+ " " + e.getMessage());
                return SaleConstants.SALE_NOT_UPDATED + " " + e.getMessage();
            }
        }
    }
    public String deleteSaleById(String id){
        Optional<Sale> saleOptional = saleRepository.findById(id);
        if(saleOptional.isEmpty()){
            LOGGER.error(SaleConstants.SALE_NOT_FOUND);
            return SaleConstants.SALE_NOT_FOUND;
        }else{
            for(Ticket ticket: saleOptional.get().getTickets()){
                ticket.setDiscountedPrice(ticket.getPrice());
                ticket.setSale(null);
                ticketRepository.save(ticket);
            }
            saleRepository.delete(saleOptional.get());
            return SaleConstants.SALE_SUCCESS_DELETE;
        }
    }
    public List<Ticket> getTickets(){
        return ticketRepository.findAll();
    }
    public Ticket findTicketById(String id){
        Optional<Ticket> ticket =  ticketRepository.findById(id);
        if(ticket.isEmpty()){
            LOGGER.error(TicketConstants.TICKET_NOT_FOUND);
            return null;
        }else{
            return ticket.get();
        }
    }
    public Double addDiscount(Double sale, Double price){
        return price*(1-sale);
    }
}
