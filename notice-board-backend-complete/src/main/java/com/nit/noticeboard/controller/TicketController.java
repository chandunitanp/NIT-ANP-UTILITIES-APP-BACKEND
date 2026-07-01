package com.nit.noticeboard.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.nit.noticeboard.dto.CreateTicketDTO;
import com.nit.noticeboard.model.Ticket;
import com.nit.noticeboard.model.User;
import com.nit.noticeboard.repository.UserRepository;
import com.nit.noticeboard.service.TicketService;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserRepository userRepo;

    // ================= CREATE TICKET =================

    @PreAuthorize("hasAnyRole('STUDENT','FACULTY')")
    @PostMapping
    public Ticket createTicket(
            @RequestBody CreateTicketDTO dto,
            Principal principal
    ) {

        User requester = userRepo
                .findByEmail(principal.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return ticketService.createTicket(dto, requester);
    }

    @PreAuthorize("hasAnyRole('STUDENT','FACULTY')")
    @GetMapping("/my")
    public List<Ticket> myTickets(
            Principal principal
    ) {

        User requester = userRepo
                .findByEmail(principal.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return ticketService.getTicketsByUser(requester);
    }
    
    @PreAuthorize("hasAnyRole('STUDENT','FACULTY','ADMIN')")
    @GetMapping("/count")
    public long getTicketCount(Principal principal) {

        User user = userRepo
                .findByEmail(principal.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return ticketService.getTicketCount(user);
    }
}