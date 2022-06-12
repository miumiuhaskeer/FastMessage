package com.miumiuhaskeer.fastmessage.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/allUsers")
    @Secured({"ROLE_USER"})
    public String testAllUsers() {
        return "Hello World!";
    }

    @GetMapping("/onlyAdmin")
    @Secured({"ROLE_ADMIN"})
    public String testOnlyAdmin() {
        return "Hello, admin!";
    }
}
