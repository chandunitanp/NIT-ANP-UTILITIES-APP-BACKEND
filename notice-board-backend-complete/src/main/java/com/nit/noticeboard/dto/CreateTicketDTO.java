package com.nit.noticeboard.dto;

import com.nit.noticeboard.model.IssueCategory;
import com.nit.noticeboard.model.TicketPriority;

public class CreateTicketDTO {

    // ================= USER DETAILS =================

    private String name;

    private String contactNo;

    private String location;

    private String roomNo;

    // ================= ISSUE DETAILS =================

    private IssueCategory category;

    private String issueDescription;

    // ================= PRIORITY =================

    private TicketPriority priority;

    // =====================================================
    // NAME
    // =====================================================

    public String getName() {
        return name;
    }

    public void setName(
            String name
    ) {
        this.name = name;
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
}