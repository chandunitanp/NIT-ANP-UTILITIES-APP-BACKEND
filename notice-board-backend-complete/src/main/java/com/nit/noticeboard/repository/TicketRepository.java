package com.nit.noticeboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nit.noticeboard.model.IssueCategory;
import com.nit.noticeboard.model.Ticket;
import com.nit.noticeboard.model.TicketStatus;
import com.nit.noticeboard.model.User;

public interface TicketRepository
        extends JpaRepository<Ticket, Long> {

    // ================= STUDENT TICKETS =================

    List<Ticket> findByCreatedBy(
            User user
    );

    // ================= FACULTY ASSIGNED =================

    List<Ticket> findByAssignedTo(
            User user
    );

    // ================= STATUS FILTER =================

    List<Ticket> findByStatus(
            TicketStatus status
    );

    // ================= OPEN TICKETS =================

    List<Ticket> findByAssignedToIsNull();

    // ================= CATEGORY FILTER =================

    List<Ticket> findByCategory(
            IssueCategory category
    );

    // ================= CATEGORY LIST =================

    List<Ticket> findByCategoryIn(
            List<IssueCategory> categories
    );

    // ================= OPEN CATEGORY =================

    List<Ticket>
    findByCategoryAndAssignedToIsNull(
            IssueCategory category
    );

    // ================= OPEN CATEGORY LIST =================

    List<Ticket>
    findByCategoryInAndAssignedToIsNull(
            List<IssueCategory> categories
    );
    
    long countByCreatedBy(User user);

    long countByAssignedTo(User user);
}