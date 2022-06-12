package com.miumiuhaskeer.fastmessage.service;

import com.miumiuhaskeer.fastmessage.bundle.ErrorBundle;
import com.miumiuhaskeer.fastmessage.model.UserDetailsImpl;
import com.miumiuhaskeer.fastmessage.model.entity.User;
import com.miumiuhaskeer.fastmessage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException(ErrorBundle.get("error.usernameNotFoundException.message"))
        );

        return UserDetailsImpl.from(user);
    }
}
