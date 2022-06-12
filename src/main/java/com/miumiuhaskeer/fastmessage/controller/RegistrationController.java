package com.miumiuhaskeer.fastmessage.controller;

import com.miumiuhaskeer.fastmessage.model.request.RegistrationRequest;
import com.miumiuhaskeer.fastmessage.model.response.ResponseEntityBuilder;
import com.miumiuhaskeer.fastmessage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sign-up")
public class RegistrationController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<ResponseEntityBuilder.SimpleResponse> registerUser(@RequestBody @Valid RegistrationRequest request) {
        userService.createUser(request.getEmail(), request.getPassword());

        return new ResponseEntityBuilder().create();
    }
}
