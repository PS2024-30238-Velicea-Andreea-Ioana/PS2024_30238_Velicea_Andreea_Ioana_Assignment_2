package com.example.untoldpsproject.dtos;

import com.example.untoldpsproject.entities.Order;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequestDto {
    private String id;
    private String name;
    private String email;
    private String action;
}
