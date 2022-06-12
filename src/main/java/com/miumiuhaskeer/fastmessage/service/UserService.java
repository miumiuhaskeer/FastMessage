package com.miumiuhaskeer.fastmessage.service;

import com.miumiuhaskeer.fastmessage.model.UserDetailsImpl;
import com.miumiuhaskeer.fastmessage.model.entity.User;

public interface UserService {
    void createUser(String email, String password);
    UserDetailsImpl authenticate(String email, String password);
    User getById(Long id);
    boolean isUserExist(String email);
}
