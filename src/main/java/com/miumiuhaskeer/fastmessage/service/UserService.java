package com.miumiuhaskeer.fastmessage.service;

import com.miumiuhaskeer.fastmessage.model.ExtendedUserDetails;
import com.miumiuhaskeer.fastmessage.model.UserDetailsImpl;
import com.miumiuhaskeer.fastmessage.model.entity.User;

public interface UserService {

    /** {@inheritDoc} */
    void createUser(String email, String password);

    /** {@inheritDoc} */
    UserDetailsImpl authenticate(String email, String password);

    /** {@inheritDoc} */
    ExtendedUserDetails getCurrentUser();

    /** {@inheritDoc} */
    User getById(Long id);

    /** {@inheritDoc} */
    boolean isUserExist(String email, String... extraEmails);

    /** {@inheritDoc} */
    boolean isUserExist(Long id, Long... extraIds);
}
