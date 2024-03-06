package com.example.untoldpsproject.repositories;

import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
}
