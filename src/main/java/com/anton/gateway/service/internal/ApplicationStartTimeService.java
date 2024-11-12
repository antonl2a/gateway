package com.anton.gateway.service.internal;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ApplicationStartTimeService {

    private Instant startTime;

    @PostConstruct
    public void init() {
        startTime = Instant.now(); // Capture the application start time
    }

    public Instant getStartTime() {
        return startTime;
    }
}
