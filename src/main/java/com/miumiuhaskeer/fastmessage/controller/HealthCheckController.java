package com.miumiuhaskeer.fastmessage.controller;

import com.miumiuhaskeer.fastmessage.statistic.FMSRequest;
import com.miumiuhaskeer.fastmessage.statistic.FMSRestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/health")
public class HealthCheckController {

    private final FMSRestTemplate fmsRestTemplate;

    @RequestMapping
    public String check() {
        return "OK";
    }

    @RequestMapping("/fms")
    public String checkFMS() {
        return fmsRestTemplate.getForObject(FMSRequest.HEALTH, null);
    }
}
