package com.sanmartindev.clockinoutbackend.dto;

public class WorkerDto {

    private String username;
    private String fullname;
    private String email;
    private Double hourlyWage;

    public WorkerDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String name) {
        this.fullname = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getHourlyWage() {
        return hourlyWage;
    }

    public void setHourlyWage(Double hourlyWage) {
        this.hourlyWage = hourlyWage;
    }
}
