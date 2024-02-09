package com.sanmartindev.clockinoutbackend.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sanmartindev.clockinoutbackend.interfaces.BonusForKilometer;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "shifts")
public class Shift implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_shift", nullable = false)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime clockIn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime pauseIn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime pauseOut;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime clockOut;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime totalTime;

    @Column(name = "value_times_hours")
    private Double valueTimesHours;

    @Column(name = "total_value")
    private Double totalValue;

    @Column(name = "shift_hourly_wage")
    private Double hourlyWage;

    @Column(name = "kilometers")
    private Double kilometers;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Shift() {
    }

    public Long getId() {
        return id;
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

    public LocalTime getClockOut() {
        return clockOut;
    }

    public void setClockOut(LocalTime clockOut) {
        this.clockOut = clockOut;
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

    public LocalTime getTotalTime() {
        return totalTime;
    }

    public Double getValueTimesHours() {
        return valueTimesHours;
    }

    public void setValueTimesHours() {
        double totalHours = this.totalTime.getHour() + (double) this.totalTime.getMinute() / 60 + (double) this.totalTime.getSecond() / 3600;
        this.valueTimesHours = totalHours * this.hourlyWage;;
    }

    public Double getHourlyWage() {
        return hourlyWage;
    }

    public void setHourlyWage(Double hourlyWage) {
        this.hourlyWage = hourlyWage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTotalTime(LocalTime clockIn, LocalTime clockOut) {
        try {
            Duration total;
            if (clockOut.isBefore(clockIn)) {
                total = Duration.between(clockIn.minusHours(12), clockOut.plusHours(12));
            } else {
                total = Duration.between(clockIn, clockOut);
            }
            if (pauseIn != null && pauseOut != null) {
                Duration pause = Duration.between(pauseIn, pauseOut);
                total = total.minus(pause);
            }
            this.totalTime = LocalTime.ofSecondOfDay(total.getSeconds());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Double getKilometers() {
        return kilometers;
    }

    public void setKilometers(Double kilometers) {
        this.kilometers = kilometers;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue() {
        this.totalValue = this.valueTimesHours + BonusForKilometer.calculateBonus(this.kilometers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shift shift = (Shift) o;
        return Objects.equals(id, shift.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}

