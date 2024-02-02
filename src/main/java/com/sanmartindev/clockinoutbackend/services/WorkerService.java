package com.sanmartindev.clockinoutbackend.services;

import com.sanmartindev.clockinoutbackend.models.Worker;
import com.sanmartindev.clockinoutbackend.repositories.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkerService {

    private final WorkerRepository workerRepository;

    @Autowired
    public WorkerService(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    public Worker save(Worker worker) {
        return workerRepository.save(worker);
    }

    public Long countAll() {
        return workerRepository.countAll();
    }
}
