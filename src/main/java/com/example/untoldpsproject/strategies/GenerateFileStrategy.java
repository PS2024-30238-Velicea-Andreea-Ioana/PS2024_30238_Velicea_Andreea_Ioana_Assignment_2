package com.example.untoldpsproject.strategies;

import com.example.untoldpsproject.entities.Order;


public interface GenerateFileStrategy {
    public final static String directoryPath = "C:\\Users\\aveli\\OneDrive\\Desktop\\proiectPs";
    String generateFile(Order order);
}
