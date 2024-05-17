package com.example.untoldpsproject.strategies;


import com.example.untoldpsproject.entities.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class Context {
    private GenerateFileStrategy generateFileStrategy;

    public String generate(Order order){
        return generateFileStrategy.generateFile(order);
    }
}
