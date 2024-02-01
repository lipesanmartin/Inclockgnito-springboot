package com.sanmartindev.clockinoutbackend.services;

import com.sanmartindev.clockinoutbackend.exceptions.ClockInvalidOperationException;
import com.sanmartindev.clockinoutbackend.models.Clock;
import com.sanmartindev.clockinoutbackend.models.User;
import com.sanmartindev.clockinoutbackend.repositories.ClockRepository;
import com.sanmartindev.clockinoutbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ClockService {

    private final ClockRepository clockRepo;

    private final UserRepository userRepo;

    @Autowired
    public ClockService(ClockRepository repo, UserRepository userRepo) {
        this.clockRepo = repo;
        this.userRepo = userRepo;
    }

    public List<Clock> findAll() {
        return clockRepo.findAll();
    }

    public List<Clock> findAllByUsername(String username) {
        return clockRepo.findAllByUsername(username);
    }

    public Clock findById(Long id) {
        return clockRepo.findById(id).orElse(null);
    }

    public Long findLastId(String username) {
        List<Clock> clocks = clockRepo.findAllByUsername(username);
        if (!clocks.isEmpty()) {
            return clocks.getLast().getId();
        } else {
            return null;
        }
    }

    public Clock clockIn(String username) {
        User user = userRepo.findByUserName(username);
        if (user == null) {
            return null;
        }
        List<Clock> clocks = clockRepo.findAllByUsername(username);
        if (!clocks.isEmpty() && clocks.get(clocks.size() - 1).getClockOut() == null) {
            throw new ClockInvalidOperationException("Error! You already have a running clock!");
        }
        ZonedDateTime serverTime = ZonedDateTime.now(ZoneId.of("GMT")).withNano(0);
        Clock clock = new Clock();
        clock.setClockIn(serverTime.toLocalTime());
        clock.setUser(user);
        return clockRepo.save(clock);
    }

    public Clock clockOut(String username) {

        Clock clock = clockRepo.findAllByUsername(username).getLast();
        if (clock.getClockOut() != null) {
            throw new ClockInvalidOperationException("Error! Clock out already done or you don't have a running clock!");
        }
        if (clock != null) {
            ZonedDateTime serverTime = ZonedDateTime.now(ZoneId.of("GMT")).withNano(0);
            clock.setClockOut(serverTime.toLocalTime());
            clock.setTotalTime(clock.getClockIn(), clock.getClockOut());
            return clockRepo.save(clock);
        }
        return null;
    }

    public Clock pauseIn(String username) {
        Clock clock = clockRepo.findAllByUsername(username).getLast();
        if (clock != null && clock.getPauseIn() == null) {
            ZonedDateTime serverTime = ZonedDateTime.now(ZoneId.of("GMT")).withNano(0);
            clock.setPauseIn(serverTime.toLocalTime());
            return clockRepo.save(clock);
        }
        throw new ClockInvalidOperationException("Error! You don't have a running clock or you already have a pause in!");
    }

    public Clock pauseOut(Long id) {
        try {
            Clock clock = clockRepo.findById(id).orElse(null);
            if (clock != null) {
                ZonedDateTime serverTime = ZonedDateTime.now(ZoneId.of("GMT")).withNano(0);
                clock.setPauseOut(serverTime.toLocalTime());
                return clockRepo.save(clock);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void delete(Long id) {
        try {
            clockRepo.deleteById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
