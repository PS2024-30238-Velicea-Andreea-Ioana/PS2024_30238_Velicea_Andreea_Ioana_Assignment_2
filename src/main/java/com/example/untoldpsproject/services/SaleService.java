package com.example.untoldpsproject.services;

import com.example.untoldpsproject.repositories.SaleRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Setter
@Getter
@AllArgsConstructor
@Service
public class SaleService {
    public final SaleRepository saleRepository;


}
