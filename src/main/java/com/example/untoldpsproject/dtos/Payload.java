package com.example.untoldpsproject.dtos;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payload {
    private String id;
    private String name;
    private String email;

    @Override
    public String toString() {
        return "Payload{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
