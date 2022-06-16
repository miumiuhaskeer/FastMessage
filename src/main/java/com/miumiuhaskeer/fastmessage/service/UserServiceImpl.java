package com.miumiuhaskeer.fastmessage.service;

import com.miumiuhaskeer.fastmessage.exception.RegistrationFailedException;
import com.miumiuhaskeer.fastmessage.exception.UserAlreadyExistException;
import com.miumiuhaskeer.fastmessage.model.UserDetailsImpl;
import com.miumiuhaskeer.fastmessage.model.entity.ERole;
import com.miumiuhaskeer.fastmessage.model.entity.Role;
import com.miumiuhaskeer.fastmessage.model.entity.User;
import com.miumiuhaskeer.fastmessage.properties.bundle.ErrorBundle;
import com.miumiuhaskeer.fastmessage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Creates user by email and password
     *
     * @param email user email (used as login)
     * @param password user password
     * @throws UserAlreadyExistException if user already exist
     * @throws RegistrationFailedException if some other error occurred
     */
    @Override
    public void createUser(String email, String password) {
        if (isUserExist(email)) {
            throw new UserAlreadyExistException();
        }

        @Valid User user = new User(email, bCryptPasswordEncoder.encode(password));
        Set<Role> roles = new HashSet<>();

        try {
            roles.add(roleService.findByName(ERole.ROLE_USER));
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            throw new RegistrationFailedException();
        }

        user.setRoles(roles);
        userRepository.save(user);
    }

    /**
     * Authenticates user by email and password
     *
     * @param email user email
     * @param password user password
     * @return user details
     * @throws org.springframework.security.core.AuthenticationException if authentication fails
     */
    @Override
    public UserDetailsImpl authenticate(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return (UserDetailsImpl) authentication.getPrincipal();
    }

    /**
     * Get user by id
     *
     * @param id user id
     * @return User Entity object
     * @throws EntityNotFoundException if user not found
     */
    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(ErrorBundle.get("error.entityNotFoundException.user.message"))
                );
    }

    @Override
    public boolean isUserExist(String email) {
        return userRepository.existsByEmail(email);
    }
}
