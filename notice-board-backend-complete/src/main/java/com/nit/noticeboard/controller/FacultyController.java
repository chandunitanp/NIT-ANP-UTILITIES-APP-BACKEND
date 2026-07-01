package com.nit.noticeboard.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.nit.noticeboard.model.Ticket;
import com.nit.noticeboard.model.User;
import com.nit.noticeboard.repository.UserRepository;
import com.nit.noticeboard.service.TicketService;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserRepository userRepo;

    private User getFaculty(Principal principal) {
        return userRepo.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
    }

    @PreAuthorize("hasRole('FACULTY')")
    @GetMapping("/tickets")
    public List<Ticket> myTickets(Principal principal) {
        return ticketService.getTicketsByUser(getFaculty(principal));
    }

    @PreAuthorize("hasRole('FACULTY')")
    @GetMapping("/tickets/{id}")
    public Ticket getTicket(@PathVariable Long id, Principal principal) {

        Ticket ticket = ticketService.getTicketById(id);

        if (!ticket.getCreatedBy().getId()
                .equals(getFaculty(principal).getId())) {
            throw new RuntimeException("Access denied");
        }

        return ticket;
    }
}