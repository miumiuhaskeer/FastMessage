package com.miumiuhaskeer.fastmessage.service;

import com.miumiuhaskeer.fastmessage.model.User;

public interface UserService {

    User createUser(String email, String password);
    void sendConfirmCodeMail(String email);

    boolean isUserExist(long id);
    boolean isUserExist(String email);
    User getById(long id);
    User getByEmail(String email);
}
