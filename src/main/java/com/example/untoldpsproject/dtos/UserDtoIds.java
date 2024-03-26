package com.example.untoldpsproject.dtos;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDtoIds {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<String> orders;
}
