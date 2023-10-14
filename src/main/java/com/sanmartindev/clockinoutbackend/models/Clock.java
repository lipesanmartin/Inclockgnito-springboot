package com.sanmartindev.clockinoutbackend.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sanmartindev.clockinoutbackend.utils.ClockUtils;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "clocks")
public class Clock implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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

    public void setClockIn(String clockIn) {
        this.clockIn = ClockUtils.parseTimeString(clockIn);
    }

    public LocalTime getClockOut() {
        return clockOut;
    }

    public void setClockOut(String clockOut) {
        this.clockOut = ClockUtils.parseTimeString(clockOut);
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

