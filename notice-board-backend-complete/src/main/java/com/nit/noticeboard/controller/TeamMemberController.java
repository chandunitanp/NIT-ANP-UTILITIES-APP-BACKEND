package com.nit.noticeboard.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.nit.noticeboard.model.Ticket;
import com.nit.noticeboard.model.TicketStatus;
import com.nit.noticeboard.model.User;
import com.nit.noticeboard.repository.UserRepository;
import com.nit.noticeboard.service.TicketService;

@RestController
@RequestMapping("/team")
public class TeamMemberController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserRepository userRepo;

    // =====================================================
    // COMMON - GET LOGGED IN TEAM MEMBER
    // =====================================================
    private User getLoggedInTeamMember(Principal principal) {

        return userRepo.findByEmail(principal.getName())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with email: " + principal.getName()
                        )
                );
    }

    // =====================================================
    // TEAM MEMBER - ASSIGNED TICKETS
    // =====================================================
    @PreAuthorize("hasRole('TEAM_MEMBER')")
    @GetMapping("/tickets")
    public List<Ticket> getAssignedTickets(Principal principal) {

        User teamMember = getLoggedInTeamMember(principal);

        return ticketService.getAssignedTickets(teamMember);
    }

    // =====================================================
    // TEAM MEMBER - UPDATE TICKET STATUS
    // =====================================================
    @PreAuthorize("hasRole('TEAM_MEMBER')")
    @PutMapping("/tickets/{ticketId}/status")
    public Ticket updateTicketStatus(

            @PathVariable Long ticketId,

            @RequestParam TicketStatus status,

            @RequestParam(required = false) String remarks,

            Principal principal
    ) {

        User teamMember = getLoggedInTeamMember(principal);

        String finalRemarks = (remarks == null || remarks.trim().isEmpty())
                ? "No remarks provided"
                : remarks;

        return ticketService.updateTicketStatus(
                ticketId,
                status,
                finalRemarks,
                teamMember
        );
    }
}