package com.example.untoldpsproject.mappers;

import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.dtos.UserDtoIds;
import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDtoIds toUserDto(User user){
        return UserDtoIds.builder().id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .orders(user.getOrders().stream().map(Order::getId).collect(Collectors.toList()))
                .build();
    }
    public static User toUser(UserDto userDto){
        return User.builder().id(userDto.getId())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .orders(userDto.getOrders())
                .build();
    }
}
