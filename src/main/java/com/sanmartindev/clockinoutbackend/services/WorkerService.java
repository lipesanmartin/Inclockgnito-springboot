package com.sanmartindev.clockinoutbackend.services;

import com.sanmartindev.clockinoutbackend.models.Worker;
import com.sanmartindev.clockinoutbackend.repositories.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkerService {


    @Autowired
    private final WorkerRepository workerRepository;

    public WorkerService(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    public Worker save(Worker worker) {
        return workerRepository.save(worker);
    }

    public Long countAll() {
        return workerRepository.countAll();
    }

    public Worker findByUsername(String username) {
        return workerRepository.findByUsername(username);
    }

    public Worker updateWage(String username, Double wage) {
        Worker worker = workerRepository.findByUsername(username);
        worker.setHourlyWage(wage);
        return workerRepository.save(worker);
    }

}
