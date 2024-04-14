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
    private String id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "Price", nullable = false)
    private Double price;
    @Column(name = "discountedPrice")
    private Double discountedPrice;

    @Column(name = "available")
    private int available;

    @JsonIgnore
    @ManyToMany(mappedBy = "tickets", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
    private List<Order> orders;

    @OneToMany(mappedBy = "ticket", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
    private List<CartItem> cartItems;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "sale_id")
    private Sale sale;
}
