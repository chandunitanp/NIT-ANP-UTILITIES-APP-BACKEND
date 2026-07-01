package com.nit.noticeboard.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "tickets")
@JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler"
})
public class Ticket {

    // ================= ID =================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= USER DETAILS =================

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String contactNo;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String roomNo;

    // ================= ISSUE DETAILS =================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssueCategory category;

    @Column(length = 5000, nullable = false)
    private String issueDescription;

    // ================= PRIORITY =================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketPriority priority =
            TicketPriority.MEDIUM;

    // ================= STATUS =================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;

    // ================= REMARKS =================

    @Column(length = 3000)
    private String resolutionRemarks;

    // ================= TIMESTAMPS =================

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime assignedAt;

    private LocalDateTime resolvedAt;

    // ================= CREATED BY =================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "created_by",
            nullable = false
    )
    private User createdBy;

    // ================= ASSIGNED TO =================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    // ================= ASSIGNED BY =================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by")
    private User assignedBy;

    // ================= CONSTRUCTOR =================

    public Ticket() {
    }

    // ================= AUTO TIMESTAMPS =================

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();

        if (status == null) {

            status = TicketStatus.OPEN;
        }

        if (priority == null) {

            priority =
                    TicketPriority.MEDIUM;
        }
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }

    // =====================================================
    // ID
    // =====================================================

    public Long getId() {
        return id;
    }

    // =====================================================
    // NAME
    // =====================================================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // =====================================================
    // EMAIL
    // =====================================================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // =====================================================
    // CONTACT NO
    // =====================================================

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(
            String contactNo
    ) {
        this.contactNo = contactNo;
    }

    // =====================================================
    // LOCATION
    // =====================================================

    public String getLocation() {
        return location;
    }

    public void setLocation(
            String location
    ) {
        this.location = location;
    }

    // =====================================================
    // ROOM NO
    // =====================================================

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(
            String roomNo
    ) {
        this.roomNo = roomNo;
    }

    // =====================================================
    // CATEGORY
    // =====================================================

    public IssueCategory getCategory() {
        return category;
    }

    public void setCategory(
            IssueCategory category
    ) {
        this.category = category;
    }

    // =====================================================
    // ISSUE DESCRIPTION
    // =====================================================

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(
            String issueDescription
    ) {
        this.issueDescription =
                issueDescription;
    }

    // =====================================================
    // PRIORITY
    // =====================================================

    public TicketPriority getPriority() {
        return priority;
    }

    public void setPriority(
            TicketPriority priority
    ) {
        this.priority = priority;
    }

    // =====================================================
    // STATUS
    // =====================================================

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(
            TicketStatus status
    ) {
        this.status = status;
    }

    // =====================================================
    // REMARKS
    // =====================================================

    public String getResolutionRemarks() {
        return resolutionRemarks;
    }

    public void setResolutionRemarks(
            String resolutionRemarks
    ) {
        this.resolutionRemarks =
                resolutionRemarks;
    }

    // =====================================================
    // CREATED AT
    // =====================================================

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // =====================================================
    // UPDATED AT
    // =====================================================

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // =====================================================
    // ASSIGNED AT
    // =====================================================

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(
            LocalDateTime assignedAt
    ) {
        this.assignedAt = assignedAt;
    }

    // =====================================================
    // RESOLVED AT
    // =====================================================

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(
            LocalDateTime resolvedAt
    ) {
        this.resolvedAt = resolvedAt;
    }

    // =====================================================
    // CREATED BY
    // =====================================================

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(
            User createdBy
    ) {
        this.createdBy = createdBy;
    }

    // =====================================================
    // ASSIGNED TO
    // =====================================================

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(
            User assignedTo
    ) {
        this.assignedTo = assignedTo;
    }

    // =====================================================
    // ASSIGNED BY
    // =====================================================

    public User getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(
            User assignedBy
    ) {
        this.assignedBy = assignedBy;
    }
}