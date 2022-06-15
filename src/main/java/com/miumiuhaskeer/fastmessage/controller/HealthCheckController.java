package com.miumiuhaskeer.fastmessage.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @RequestMapping("/health")
    public String check() {
        return "OK";
    }
}
