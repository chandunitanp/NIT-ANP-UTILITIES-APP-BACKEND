package com.nit.noticeboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nit.noticeboard.model.Ticket;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // =====================================================
    // RESET PASSWORD MAIL
    // =====================================================

    @Async
    public void sendResetToken(
            String to,
            String token
    ) {

        try {

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setTo(to);

            message.setSubject(
                    "NIT ANP Utilities App - Password Reset"
            );

            message.setText(

                    "Your password reset code is:\n\n"

                    + token

                    + "\n\n"

                    + "If you did not request this,"
                    + " please ignore this email."
            );

            mailSender.send(message);

        } catch (Exception e) {
            System.out.println("Mail Error: " + e.getMessage());
        }
    }

    // =====================================================
    // NEW TICKET CREATED
    // =====================================================

    @Async
    public void sendTicketNotification(
            Ticket ticket
    ) {

        try {

            String adminMail;
            String serviceName;

            switch (ticket.getCategory()) {

                case NEW_IP_CONNECTION:
                case IP_PHONE_NOT_WORKING:
                case INCOMING_OUTGOING:
                case RECEIVER_MIC_SPEAKER:

                    adminMail =
                            "teleservices@nitandhra.ac.in";

                    serviceName =
                            "Teleservices";

                    break;

                case WEB_REQUEST:

                    adminMail =
                            "web@nitandhra.ac.in";

                    serviceName =
                            "WEB";

                    break;

                default:

                    adminMail =
                            "network@nitandhra.ac.in";

                    serviceName =
                            "Network";
            }

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setTo(adminMail);

            message.setSubject(
                    "New "
                    + serviceName
                    + " Ticket Created"
            );

            message.setText(
                    "A new ticket has been created.\n\n"

                    + "Ticket ID: "
                    + ticket.getId()
                    + "\n\n"

                    + "Priority: "
                    + ticket.getPriority()
                    + "\n"

                    + "Status: "
                    + ticket.getStatus()
                    + "\n\n"

                    + "Name: "
                    + ticket.getName()
                    + "\n"

                    + "Email: "
                    + ticket.getEmail()
                    + "\n"

                    + "Contact No: "
                    + ticket.getContactNo()
                    + "\n"

                    + "Location: "
                    + ticket.getLocation()
                    + "\n"

                    + "Room No: "
                    + ticket.getRoomNo()
                    + "\n"

                    + "Category: "
                    + ticket.getCategory()
                    + "\n\n"

                    + "Issue Description:\n"
                    + ticket.getIssueDescription()
            );

            mailSender.send(message);

        } catch (Exception e) {
            System.out.println("Mail Error: " + e.getMessage());
        }
    }

    // =====================================================
    // TICKET ASSIGNED TO FACULTY
    // =====================================================

    @Async
    public void sendAssignmentMail(
            Ticket ticket
    ) {

        try {

            if (ticket.getAssignedTo() == null) {
                return;
            }

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setTo(
                    ticket.getAssignedTo()
                            .getEmail()
            );

            message.setSubject(
                    "Ticket Assigned - ID "
                    + ticket.getId()
            );

            message.setText(
                    "A ticket has been assigned "
                    + "to you.\n\n"

                    + "Ticket ID: "
                    + ticket.getId()
                    + "\n"

                    + "Priority: "
                    + ticket.getPriority()
                    + "\n"

                    + "Category: "
                    + ticket.getCategory()
                    + "\n"

                    + "Location: "
                    + ticket.getLocation()
                    + "\n"

                    + "Room No: "
                    + ticket.getRoomNo()
                    + "\n\n"

                    + "Issue:\n"
                    + ticket.getIssueDescription()
            );

            mailSender.send(message);

        } catch (Exception e) {
            System.out.println("Mail Error: " + e.getMessage());
        }
    }

    // =====================================================
    // STATUS UPDATE MAIL TO ADMIN
    // =====================================================

    @Async
    public void sendStatusUpdateMail(
            Ticket ticket
    ) {

        try {

            String adminMail;

            switch (ticket.getCategory()) {

                case NEW_IP_CONNECTION:
                case IP_PHONE_NOT_WORKING:
                case INCOMING_OUTGOING:
                case RECEIVER_MIC_SPEAKER:

                    adminMail =
                            "teleservices@nitandhra.ac.in";

                    break;

                case WEB_REQUEST:

                    adminMail =
                            "web@nitandhra.ac.in";

                    break;

                default:

                    adminMail =
                            "network@nitandhra.ac.in";
            }

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setTo(adminMail);

            message.setSubject(
                    "Ticket Status Updated - ID "
                    + ticket.getId()
            );

            message.setText(
                    "Ticket status has been updated.\n\n"

                    + "Ticket ID: "
                    + ticket.getId()
                    + "\n"

                    + "Updated Status: "
                    + ticket.getStatus()
                    + "\n"

                    + "Assigned Faculty: "
                    + (
                    ticket.getAssignedTo() != null
                            ? ticket.getAssignedTo().getName()
                            : "Not Assigned"
                    )
                    + "\n\n"

                    + "Remarks:\n"
                    + (
                    ticket.getResolutionRemarks() != null
                            ? ticket.getResolutionRemarks()
                            : "No remarks"
                    )
            );

            mailSender.send(message);

        } catch (Exception e) {
            System.out.println("Mail Error: " + e.getMessage());
        }
    }

    // =====================================================
    // STATUS UPDATE MAIL TO USER
    // =====================================================

    @Async
    public void sendUserStatusUpdate(
            Ticket ticket
    ) {

        try {

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setTo(ticket.getEmail());

            message.setSubject(
                    "Your Ticket Status Updated - ID "
                    + ticket.getId()
            );

            message.setText(
                    "Your complaint ticket status "
                    + "has been updated.\n\n"

                    + "Ticket ID: "
                    + ticket.getId()
                    + "\n"

                    + "Category: "
                    + ticket.getCategory()
                    + "\n"

                    + "Current Status: "
                    + ticket.getStatus()
                    + "\n\n"

                    + "Assigned Faculty: "
                    + (
                    ticket.getAssignedTo() != null
                            ? ticket.getAssignedTo().getName()
                            : "Not Assigned Yet"
                    )
                    + "\n\n"

                    + "Remarks:\n"
                    + (
                    ticket.getResolutionRemarks() != null
                            ? ticket.getResolutionRemarks()
                            : "No remarks available"
                    )

                    + "\n\nThank you."
            );

            mailSender.send(message);

        } catch (Exception e) {
            System.out.println("Mail Error: " + e.getMessage());
        }
    }
}