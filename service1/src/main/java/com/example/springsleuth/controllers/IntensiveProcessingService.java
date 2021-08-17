package com.example.springsleuth.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Service;

@Service
public class IntensiveProcessingService {
    private static final Logger LOG = LoggerFactory.getLogger(IntensiveProcessingService.class);

    @NewSpan("cpuIntensiveMethodSimulation")
    public void cpuIntensiveMethodSimulation() throws InterruptedException {
        LOG.info("Executing CPU intensive method");
        Thread.sleep(2000L);
    }
}
