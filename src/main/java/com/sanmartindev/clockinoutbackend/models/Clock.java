package com.sanmartindev.clockinoutbackend.models;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @Column(name = "id_clock", nullable = false)
    private Long id;


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

