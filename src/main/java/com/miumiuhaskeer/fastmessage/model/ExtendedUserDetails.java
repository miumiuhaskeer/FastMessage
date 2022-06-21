package com.miumiuhaskeer.fastmessage.model;

import org.springframework.security.core.userdetails.UserDetails;

public abstract class ExtendedUserDetails implements UserDetails {

    public abstract Long getId();
    public abstract String getEmail();

    @Override
    public final String getUsername() {
        return getEmail();
    }
}
