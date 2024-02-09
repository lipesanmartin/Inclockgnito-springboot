package com.sanmartindev.clockinoutbackend.services;

import com.sanmartindev.clockinoutbackend.Dto.ShiftDto;
import com.sanmartindev.clockinoutbackend.exceptions.ClockInvalidOperationException;
import com.sanmartindev.clockinoutbackend.models.Shift;
import com.sanmartindev.clockinoutbackend.models.User;
import com.sanmartindev.clockinoutbackend.models.Worker;
import com.sanmartindev.clockinoutbackend.repositories.ShiftRepository;
import com.sanmartindev.clockinoutbackend.repositories.UserRepository;
import com.sanmartindev.clockinoutbackend.repositories.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShiftService {

    private final ShiftRepository clockRepo;

    private final UserRepository userRepo;

    private final WorkerRepository workerRepo;

    @Autowired
    public ShiftService(ShiftRepository repo, UserRepository userRepo, WorkerRepository workerRepo) {
        this.clockRepo = repo;
        this.userRepo = userRepo;
        this.workerRepo = workerRepo;
    }


    public List<ShiftDto> findAllByUsername(String username) {
        List<Shift> shifts = clockRepo.findAllByUsername(username);
        List<ShiftDto> shiftDtos = new ArrayList<>();
        for (Shift shift : shifts) {
            shiftDtos.add(createDto(shift));
        }
        return shiftDtos;
    }

    public Long findLastId(String username) {
        List<Shift> shifts = clockRepo.findAllByUsername(username);
        if (!shifts.isEmpty()) {
            return shifts.getLast().getId();
        } else {
            return null;
        }
    }

    public ShiftDto clockIn(String username) {
        User user = userRepo.findByUserName(username);
        Worker worker = workerRepo.findByUsername(username);
        if (user == null || worker == null) {
            return null;
        }
        List<Shift> shifts = clockRepo.findAllByUsername(username);
        if (!shifts.isEmpty() && shifts.get(shifts.size() - 1).getClockOut() == null) {
            throw new ClockInvalidOperationException("Error! You already have a running shift!");
        }
        ZonedDateTime serverTime = ZonedDateTime.now(ZoneId.of("GMT")).withNano(0);
        Shift shift = new Shift();
        shift.setDate(serverTime.toLocalDate());
        shift.setClockIn(serverTime.toLocalTime());
        shift.setUser(user);
        shift.setHourlyWage(worker.getHourlyWage());
        clockRepo.save(shift);
        return createDto(shift);
    }

    public ShiftDto clockOut(String username) {
        Shift shift = clockRepo.findAllByUsername(username).getLast();
        if (shift.getClockOut() != null) {
            throw new ClockInvalidOperationException("Error! Shift out already done or you don't have a running shift!");
        }
        if (shift != null) {
            ZonedDateTime serverTime = ZonedDateTime.now(ZoneId.of("GMT")).withNano(0);
            shift.setClockOut(serverTime.toLocalTime());
            shift.setTotalTime(shift.getClockIn(), shift.getClockOut());
            shift.setValueTimesHours();
            clockRepo.save(shift);
            return createDto(shift);

        }
        return null;
    }

    public ShiftDto pauseIn(String username) {
        Shift shift = clockRepo.findAllByUsername(username).getLast();
        if (shift != null && shift.getPauseIn() == null && shift.getPauseOut() == null && shift.getClockOut() == null) {
            ZonedDateTime serverTime = ZonedDateTime.now(ZoneId.of("GMT")).withNano(0);
            shift.setPauseIn(serverTime.toLocalTime());
            clockRepo.save(shift);
            return createDto(shift);
        }
        throw new ClockInvalidOperationException("Error! You don't have a running shift or you already have a pause in!");
    }

    public ShiftDto pauseOut(String username) {
        Shift shift = clockRepo.findAllByUsername(username).getLast();
        if (shift != null && shift.getPauseIn() != null && shift.getPauseOut() == null && shift.getClockOut() == null) {
            ZonedDateTime serverTime = ZonedDateTime.now(ZoneId.of("GMT")).withNano(0);
            shift.setPauseOut(serverTime.toLocalTime());
            clockRepo.save(shift);
            return createDto(shift);
        }
        throw new ClockInvalidOperationException("Error! You don't have a running shift or you already have a pause out!");
    }

    public ShiftDto setKilometers(String username, Double kilometers) {
        Shift shift = clockRepo.findAllByUsername(username).getLast();
        if (shift != null) {
            shift.setKilometers(kilometers);
            shift.setTotalValue();
            clockRepo.save(shift);
            return createDto(shift);
        }
        return null;
    }

    private ShiftDto createDto(Shift shift) {
        ShiftDto shiftDto = new ShiftDto();
        shiftDto.setDate(shift.getDate());
        shiftDto.setClockIn(shift.getClockIn());
        shiftDto.setClockOut(shift.getClockOut());
        shiftDto.setPauseIn(shift.getPauseIn());
        shiftDto.setPauseOut(shift.getPauseOut());
        shiftDto.setTotalTime(shift.getTotalTime());
        shiftDto.setHourlyWage(shift.getHourlyWage());
        shiftDto.setKilometers(shift.getKilometers());
        shiftDto.setValueTimesHours(shift.getValueTimesHours());
        shiftDto.setTotalValue(shift.getTotalValue());
        return shiftDto;
    }

}
