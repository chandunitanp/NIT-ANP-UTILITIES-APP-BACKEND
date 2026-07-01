package com.nit.noticeboard.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nit.noticeboard.dto.CreateTicketDTO;
import com.nit.noticeboard.model.IssueCategory;
import com.nit.noticeboard.model.Role;
import com.nit.noticeboard.model.TeamType;
import com.nit.noticeboard.model.Ticket;
import com.nit.noticeboard.model.TicketPriority;
import com.nit.noticeboard.model.TicketStatus;
import com.nit.noticeboard.model.User;
import com.nit.noticeboard.repository.TicketRepository;
import com.nit.noticeboard.repository.UserRepository;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    // =========================================================
    // CREATE TICKET (STUDENT / FACULTY ONLY)
    // =========================================================
    public Ticket createTicket(CreateTicketDTO dto, User requester) {

        if (requester.getRole() != Role.STUDENT &&
            requester.getRole() != Role.FACULTY) {
            throw new RuntimeException("Only STUDENT or FACULTY can create tickets");
        }

        Ticket ticket = new Ticket();

        ticket.setName(dto.getName());
        ticket.setEmail(requester.getEmail());
        ticket.setContactNo(dto.getContactNo());
        ticket.setLocation(dto.getLocation());
        ticket.setRoomNo(dto.getRoomNo());

        ticket.setCategory(dto.getCategory());
        ticket.setIssueDescription(dto.getIssueDescription());

        ticket.setPriority(TicketPriority.MEDIUM);

        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedBy(requester);

        Ticket saved = ticketRepository.save(ticket);

        emailService.sendTicketNotification(saved);

        return saved;
    }

    // =========================================================
    // GET USER TICKETS
    // =========================================================
    public List<Ticket> getTicketsByUser(User user) {
        return ticketRepository.findByCreatedBy(user);
    }

    // =========================================================
    // GET ASSIGNED TICKETS (TEAM USER)
    // =========================================================
    public List<Ticket> getAssignedTickets(User user) {
        return ticketRepository.findByAssignedTo(user);
    }
    
    public Ticket updatePriority(
            Long ticketId,
            TicketPriority priority,
            User admin) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() ->
                        new RuntimeException("Ticket not found"));

        if (admin.getTeamType() == null) {
            throw new RuntimeException(
                    "Admin team type not assigned");
        }

        if (!belongsToTeam(
                ticket.getCategory(),
                admin.getTeamType())) {

            throw new RuntimeException(
                    "Cannot update priority of another department ticket");
        }

        ticket.setPriority(priority);

        return ticketRepository.save(ticket);
    }
    // =========================================================
    // GET TICKET BY ID
    // =========================================================
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    // =========================================================
    // TEAM CATEGORY MAPPING
    // =========================================================
    private boolean belongsToTeam(IssueCategory category, TeamType teamType) {

        if (teamType == null) return false;

        if (teamType == TeamType.NETWORK) {
            return category == IssueCategory.NEW_LAN_CONNECTION
                    || category == IssueCategory.LAN_CONNECT_DATA_PORT
                    || category == IssueCategory.PRINTER_ISSUE
                    || category == IssueCategory.SYSTEM_COMPLAINT
                    || category == IssueCategory.OS_APP_INSTALLATIONS
                    || category == IssueCategory.OFFICE_365_INSTALL
                    || category == IssueCategory.MEETINGS_CONFERENCES
                    || category == IssueCategory.PROJECTOR_COMPLAINT
                    || category == IssueCategory.LAPTOP_COMPLAINT
                    || category == IssueCategory.CC_CAM_CONCERNS
                    || category == IssueCategory.INTERACTIVEDISPLAY_ISSUE
                    || category == IssueCategory.WIFI_ISSUES
                    || category == IssueCategory.OTHER;
        }

        if (teamType == TeamType.TELESERVICES) {
            return category == IssueCategory.NEW_IP_CONNECTION
                    || category == IssueCategory.IP_PHONE_NOT_WORKING
                    || category == IssueCategory.INCOMING_OUTGOING
                    || category == IssueCategory.RECEIVER_MIC_SPEAKER;
        }

        if (teamType == TeamType.WEB) {
            return category == IssueCategory.WEB_REQUEST;
        }

        return false;
    }

    // =========================================================
    // ADMIN - GET ALL TEAM TICKETS
    // =========================================================
    public List<Ticket> getAllTickets(User admin) {

        if (admin.getTeamType() == null) {
            throw new RuntimeException("Admin team type not assigned");
        }

        return ticketRepository.findAll()
                .stream()
                .filter(ticket ->
                        belongsToTeam(ticket.getCategory(), admin.getTeamType())
                )
                .collect(Collectors.toList());
    }

    // =========================================================
    // ADMIN - UNASSIGNED TICKETS
    // =========================================================
    public List<Ticket> getUnassignedTickets(User admin) {

        if (admin.getTeamType() == null) {
            throw new RuntimeException("Admin team type not assigned");
        }

        return ticketRepository.findByAssignedToIsNull()
                .stream()
                .filter(ticket ->
                        belongsToTeam(ticket.getCategory(), admin.getTeamType())
                )
                .collect(Collectors.toList());
    }

    // =========================================================
    // ADMIN - ASSIGN TICKET
    // =========================================================
    public Ticket assignTicket(Long ticketId, Long userId, User admin) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        User teamMember = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (admin.getTeamType() == null) {
            throw new RuntimeException("Admin team type not assigned");
        }

        if (teamMember.getTeamType() == null) {
            throw new RuntimeException("User team type not assigned");
        }

        // IMPORTANT FIX: no TEAM_MEMBER role dependency
        if (teamMember.getRole() == Role.ADMIN) {
            throw new RuntimeException("Admin cannot be assigned tickets");
        }

        if (teamMember.getTeamType() != admin.getTeamType()) {
            throw new RuntimeException("Team mismatch");
        }

        if (!belongsToTeam(ticket.getCategory(), teamMember.getTeamType())) {
            throw new RuntimeException("Cannot assign ticket from other department");
        }

        ticket.setAssignedTo(teamMember);
        ticket.setAssignedBy(admin);
        ticket.setAssignedAt(LocalDateTime.now());
        ticket.setStatus(TicketStatus.ASSIGNED);

        Ticket saved = ticketRepository.save(ticket);

        emailService.sendAssignmentMail(saved);

        return saved;
    }

    // =========================================================
    // TEAM USER - UPDATE STATUS
    // =========================================================
    public Ticket updateTicketStatus(
            Long ticketId,
            TicketStatus status,
            String remarks,
            User teamUser
    ) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (ticket.getAssignedTo() == null) {
            throw new RuntimeException("Ticket not assigned yet");
        }

        if (!ticket.getAssignedTo().getId().equals(teamUser.getId())) {
            throw new RuntimeException("You are not assigned to this ticket");
        }

        if (teamUser.getTeamType() == null) {
            throw new RuntimeException("Team not assigned");
        }

        if (!belongsToTeam(ticket.getCategory(), teamUser.getTeamType())) {
            throw new RuntimeException("Cannot update other team ticket");
        }

        ticket.setStatus(status);

        ticket.setResolutionRemarks(
                (remarks == null || remarks.trim().isEmpty())
                        ? "No remarks provided"
                        : remarks.trim()
        );

        if (status == TicketStatus.RESOLVED) {
            ticket.setResolvedAt(LocalDateTime.now());
        }

        Ticket updated = ticketRepository.save(ticket);

        emailService.sendStatusUpdateMail(updated);
        emailService.sendUserStatusUpdate(updated);

        return updated;
    }
    
    public long getTicketCount(User user) {

        if (user.getRole() == Role.ADMIN) {
            return ticketRepository.countByAssignedTo(user);
        }

        return ticketRepository.countByCreatedBy(user);
    }
}