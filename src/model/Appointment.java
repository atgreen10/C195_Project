package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Appointment {

    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String apptType;
//    private LocalDate startDate;
//    private LocalTime startTime;
//    private LocalDate endDate;
//    private LocalTime endTime;
    private int customerID;
    private int userID;
    private int contactID;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public Appointment() {
    }

    ;

    public Appointment(int appointmentID, String title, String description, String location, int contactID,
                       String apptType, LocalDateTime startDateTime, LocalDateTime endDateTime, int customerID,
                       int userID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contactID = contactID;
        this.apptType = apptType;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.customerID = customerID;
        this.userID = userID;

    }


    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getApptType() {
        return apptType;
    }

    public void setApptType(String apptType) {
        this.apptType = apptType;
    }

//    public LocalTime getApptStartTime() {
//        return startTime;
//    }
//
//    public void setApptStartTime(LocalTime startTime) {
//        this.startTime = startTime;
//    }
//
//    public LocalDate getApptStartDate() {
//        return startDate;
//    }
//
//    public void setApptStartDate(LocalDate startDate) {
//        this.startDate = startDate;
//    }
//
//    public LocalTime getApptEndTime() {
//        return endTime;
//    }
//
//    public void setApptEndTime(LocalTime endTime) {
//        this.endTime = endTime;
//    }
//
//    public LocalDate getApptEndDate() {
//        return endDate;
//    }
//
//    public void setApptEndDate(LocalDate endDate) {
//        this.endDate = endDate;
//    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    @Override
    public String toString() {
        return String.valueOf((getContactID()));
    }
}