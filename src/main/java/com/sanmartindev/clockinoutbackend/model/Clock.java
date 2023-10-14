package com.sanmartindev.clockinoutbackend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "clocks")
public class Clock implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clock_id", nullable = false)
    private Long id;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime clockIn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime clockOut;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime totalTime;

    public Long getId() {
        return id;
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

    public LocalTime getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(LocalTime clockIn, LocalTime clockOut) {
        Duration duration = Duration.between(clockIn, clockOut);
        this.totalTime = LocalTime.ofSecondOfDay(duration.getSeconds());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clock clock = (Clock) o;
        return Objects.equals(id, clock.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}

