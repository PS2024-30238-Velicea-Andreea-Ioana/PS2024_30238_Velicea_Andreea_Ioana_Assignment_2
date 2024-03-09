package com.example.untoldpsproject.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(name = "Type", nullable = false)
    private String type;

    @Column(name = "Price", nullable = false)
    private Double price;

    @Column(name = "Quantity", nullable = false)
    private int quantity;

    @JsonIgnore
    @ManyToMany(mappedBy = "tickets")
    private Set<Order> orders = new HashSet<>();

    public Set<UUID> convertOrdersToIds (){
        Set<UUID> orderIds = new HashSet<>();
        if(!orders.isEmpty()) {
            for (Order order : orders) {
                orderIds.add(order.getId());
            }
        }
        return orderIds;
    }
}
