package com.example.untoldpsproject.repositories;

import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {

}
