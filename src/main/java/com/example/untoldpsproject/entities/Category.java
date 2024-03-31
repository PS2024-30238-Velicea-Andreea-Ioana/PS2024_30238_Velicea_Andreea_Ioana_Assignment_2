package com.example.untoldpsproject.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    @Column(name = "tip")
    private String tip;

    @OneToMany(mappedBy = "category",cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
    private List<Ticket> tickets;

    @Column(name = "startDate")
    private LocalDate startDate;

    @Column(name = "finishDate")
    private LocalDate finishDate;
}
