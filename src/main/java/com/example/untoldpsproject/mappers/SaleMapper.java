package com.example.untoldpsproject.mappers;

import com.example.untoldpsproject.dtos.SaleDto;
import com.example.untoldpsproject.entities.Sale;

public class SaleMapper {
    public static SaleDto toSaleDto(Sale sale){
        return SaleDto.builder()
                .id(sale.getId())
                .discountedPrice(sale.getDiscountedPrice())
                .discountPercentage(sale.getDiscountPercentage())
                .build();
    }
    public static Sale toSale(SaleDto saleDto){
        return Sale.builder()
                .id(saleDto.getId())
                .discountedPrice(saleDto.getDiscountedPrice())
                .discountPercentage(saleDto.getDiscountPercentage())
                .build();
    }
}
