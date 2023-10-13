package com.sanmartindev.clockinoutbackend.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Clock {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private Instant clockIn;
    private Instant clockOut;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



}
