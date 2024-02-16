package com.inclockgnito.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ShiftDto {

    private LocalDate date;


    private LocalTime clockIn;
    private LocalTime pauseIn;
    private LocalTime pauseOut;
    private LocalTime clockOut;
    private LocalTime totalTime;
    private Double hourlyWage;

    private Double kilometers;

    private Double valueTimesHours;

    private Double totalValue;

    public ShiftDto() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getClockIn() {
        return clockIn;
    }

    public void setClockIn(LocalTime clockIn) {
        this.clockIn = clockIn;
    }

    public LocalTime getPauseIn() {
        return pauseIn;
    }

    public void setPauseIn(LocalTime pauseIn) {
        this.pauseIn = pauseIn;
    }

    public LocalTime getPauseOut() {
        return pauseOut;
    }

    public void setPauseOut(LocalTime pauseOut) {
        this.pauseOut = pauseOut;
    }

    public LocalTime getClockOut() {
        return clockOut;
    }

    public void setClockOut(LocalTime clockOut) {
        this.clockOut = clockOut;
    }

    public LocalTime getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(LocalTime totalTime) {
        this.totalTime = totalTime;
    }

    public Double getHourlyWage() {
        return hourlyWage;
    }

    public void setHourlyWage(Double hourlyWage) {
        this.hourlyWage = hourlyWage;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public Double getKilometers() {
        return kilometers;
    }

    public void setKilometers(Double kilometers) {
        this.kilometers = kilometers;
    }

    public Double getValueTimesHours() {
        return valueTimesHours;
    }

    public void setValueTimesHours(Double valueTimesHours) {
        this.valueTimesHours = valueTimesHours;
    }
}
