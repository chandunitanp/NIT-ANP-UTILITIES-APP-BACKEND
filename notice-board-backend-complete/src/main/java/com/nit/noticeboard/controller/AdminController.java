package com.nit.noticeboard.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.nit.noticeboard.model.Role;
import com.nit.noticeboard.model.Ticket;
import com.nit.noticeboard.model.TicketPriority;
import com.nit.noticeboard.model.User;
import com.nit.noticeboard.repository.UserRepository;
import com.nit.noticeboard.service.TicketService;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserRepository userRepo;

    // ================= GET ADMIN =================

    private User getAdmin(
            Principal principal
    ) {

        return userRepo
                .findByEmail(
                        principal.getName()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Admin not found"
                        )
                );
    }

    // ================= ALL TICKETS =================

    @GetMapping("/tickets")
    public List<Ticket> allTickets(
            Principal principal
    ) {

        User admin =
                getAdmin(principal);

        return ticketService
                .getAllTickets(admin);
    }

    // ================= OPEN TICKETS =================

    @GetMapping("/tickets/open")
    public List<Ticket> openTickets(
            Principal principal
    ) {

        User admin =
                getAdmin(principal);

        return ticketService
                .getUnassignedTickets(admin);
    }

    // ================= FACULTY LIST =================

    @GetMapping("/team-members")
    public List<User> facultyList(
            Principal principal
    ) {

        User admin =
                getAdmin(principal);

        return userRepo
                .findByRoleAndTeamType(
                        Role.TEAM_MEMBER,
                        admin.getTeamType()
                );
    }

    // ================= ASSIGN TICKET =================

    @PutMapping(
            "/tickets/{ticketId}/assign/{facultyId}"
    )
    public Ticket assignTicket(

            @PathVariable Long ticketId,

            @PathVariable Long facultyId,

            Principal principal
    ) {

        User admin =
                getAdmin(principal);

        return ticketService
                .assignTicket(
                        ticketId,
                        facultyId,
                        admin
                );
    }
    
    @PutMapping("/tickets/{id}/priority")
    public Ticket updatePriority(
            @PathVariable Long id,
            @RequestParam TicketPriority priority,
            Principal principal) {

        User admin = getAdmin(principal);

        return ticketService.updatePriority(
                id,
                priority,
                admin);
    }
}