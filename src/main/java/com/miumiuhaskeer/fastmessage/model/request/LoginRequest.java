package com.miumiuhaskeer.fastmessage.model.request;

import com.miumiuhaskeer.fastmessage.annotation.Password;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    //@Email
    @Size(max = 50)
    private String email;

    @Password
    private String password;
}
