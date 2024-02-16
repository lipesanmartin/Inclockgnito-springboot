package com.inclockgnito.services;

import com.inclockgnito.exceptions.ClockInvalidOperationException;
import com.inclockgnito.repositories.ShiftRepository;
import com.inclockgnito.repositories.UserRepository;
import com.inclockgnito.repositories.WorkerRepository;
import com.inclockgnito.dto.ShiftDto;
import com.inclockgnito.models.Shift;
import com.inclockgnito.models.User;
import com.inclockgnito.models.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepo;

    private final UserRepository userRepo;

    private final WorkerRepository workerRepo;

    @Autowired
    public ShiftService(ShiftRepository repo, UserRepository userRepo, WorkerRepository workerRepo) {
        this.shiftRepo = repo;
        this.userRepo = userRepo;
        this.workerRepo = workerRepo;
    }

    public List<ShiftDto> findAllByUsername(String username) {
        List<Shift> shifts = shiftRepo.findAllByUsername(username);
        List<ShiftDto> shiftDtos = new ArrayList<>();
        for (Shift shift : shifts) {
            shiftDtos.add(createDto(shift));
        }
        return shiftDtos;
    }

    public Long findLastId(String username) {
        List<Shift> shifts = shiftRepo.findAllByUsername(username);
        if (!shifts.isEmpty()) {
            return shifts.getLast().getId();
        } else {
            return null;
        }
    }

    public ShiftDto findLastShift(String username) {
        List<Shift> shifts = shiftRepo.findAllByUsername(username);
        if (!shifts.isEmpty()) {
            return createDto(shifts.getLast());
        } else {
            return null;
        }
    }

    public ShiftDto clockIn(String username) {
        User user = userRepo.findByUserNameIgnoreCase(username);
        Worker worker = workerRepo.findByUsernameIgnoreCase(username);
        if (user == null || worker == null) {
            return null;
        }
        List<Shift> shifts = shiftRepo.findAllByUsername(username);
        if (!shifts.isEmpty() && shifts.get(shifts.size() - 1).getClockOut() == null) {
            throw new ClockInvalidOperationException("Error! You already have a running shift!");
        }
        ZonedDateTime serverTime = ZonedDateTime.now(ZoneId.of("GMT")).withNano(0);
        Shift shift = new Shift();
        shift.setDate(serverTime.toLocalDate());
        shift.setClockIn(serverTime.toLocalTime());
        shift.setUser(user);
        if (worker.getHourlyWage() != null) {
            shift.setHourlyWage(worker.getHourlyWage());
        } else {
            shift.setHourlyWage(0.0);
        }
        shiftRepo.save(shift);
        return createDto(shift);
    }

    public ShiftDto clockOut(String username) {
        Shift shift = shiftRepo.findAllByUsername(username).getLast();
        if (shift.getClockOut() != null || (shift.getPauseIn() != null && shift.getPauseOut() == null)) {
            throw new ClockInvalidOperationException("Error! Clock out already done or you don't have a running shift!");
        }
        ZonedDateTime serverTime = ZonedDateTime.now(ZoneId.of("GMT")).withNano(0);
        shift.setClockOut(serverTime.toLocalTime());
        shift.setTotalTime(shift.getClockIn(), shift.getClockOut());
        shift.setValueTimesHours();
        shiftRepo.save(shift);
        return createDto(shift);
    }

    public ShiftDto pauseIn(String username) {
        Shift shift = shiftRepo.findAllByUsername(username).getLast();
        if (shift != null && shift.getPauseIn() == null && shift.getPauseOut() == null && shift.getClockOut() == null) {
            ZonedDateTime serverTime = ZonedDateTime.now(ZoneId.of("GMT")).withNano(0);
            shift.setPauseIn(serverTime.toLocalTime());
            shiftRepo.save(shift);
            return createDto(shift);
        }
        throw new ClockInvalidOperationException("Error! You don't have a running shift or you already have a pause in!");
    }

    public ShiftDto pauseOut(String username) {
        Shift shift = shiftRepo.findAllByUsername(username).getLast();
        if (shift != null && shift.getPauseIn() != null && shift.getPauseOut() == null && shift.getClockOut() == null) {
            ZonedDateTime serverTime = ZonedDateTime.now(ZoneId.of("GMT")).withNano(0);
            shift.setPauseOut(serverTime.toLocalTime());
            shiftRepo.save(shift);
            return createDto(shift);
        }
        throw new ClockInvalidOperationException("Error! You don't have a running shift or you already have a pause out!");
    }

    public ShiftDto setKilometers(String username, Double kilometers) {
        Shift shift = shiftRepo.findAllByUsername(username).getLast();
        if (shift != null) {
            shift.setKilometers(kilometers);
            shift.setTotalValue();
            shiftRepo.save(shift);
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
