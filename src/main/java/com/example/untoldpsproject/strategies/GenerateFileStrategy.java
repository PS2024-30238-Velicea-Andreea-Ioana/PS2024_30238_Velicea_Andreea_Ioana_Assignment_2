package com.example.untoldpsproject.strategies;

import com.example.untoldpsproject.entities.Order;


public interface GenerateFileStrategy {
    public final static String directoryPath = "C:\\Users\\aveli\\IdeaProjects\\UntoldPsProject\\src\\main\\resources\\static\\files";
    String generateFile(Order order);
}
