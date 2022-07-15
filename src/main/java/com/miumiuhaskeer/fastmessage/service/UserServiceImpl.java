package com.miumiuhaskeer.fastmessage.service;

import com.miumiuhaskeer.fastmessage.exception.NotAuthenticatedException;
import com.miumiuhaskeer.fastmessage.exception.RegistrationFailedException;
import com.miumiuhaskeer.fastmessage.exception.UserAlreadyExistException;
import com.miumiuhaskeer.fastmessage.model.ExtendedUserDetails;
import com.miumiuhaskeer.fastmessage.model.UserDetailsImpl;
import com.miumiuhaskeer.fastmessage.model.entity.ERole;
import com.miumiuhaskeer.fastmessage.model.entity.Role;
import com.miumiuhaskeer.fastmessage.model.entity.User;
import com.miumiuhaskeer.fastmessage.model.kafka.UserInfoKafka;
import com.miumiuhaskeer.fastmessage.properties.bundle.ErrorBundle;
import com.miumiuhaskeer.fastmessage.repository.postgresql.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final KafkaTemplate<Long, UserInfoKafka> userInfoKafkaTemplate;

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

        // TODO change to normal method
        UserInfoKafka userInfoKafka = new UserInfoKafka(user);
        ListenableFuture<SendResult<Long, UserInfoKafka>> future
                = userInfoKafkaTemplate.send("createUserInfo", user.getId(), userInfoKafka);

        future.addCallback(System.out::println, System.err::println);
        userInfoKafkaTemplate.flush();
    }

    // TODO change to ExtendedUserDetails
    /**
     * Authenticates user by email and password
     *
     * @param email user email
     * @param password user password
     * @return user details
     * @throws org.springframework.security.core.AuthenticationException if authentication fails
     * @throws org.springframework.security.authentication.BadCredentialsException thrown if incorrect
     *              credentials are presented. Whilst the above exceptions are optional,
     *              an AuthenticationManager must always test credentials
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
     * Get current user details
     *
     * @return ExtendedUserDetails user details with id
     * @throws NotAuthenticatedException if user not authenticated or principal not implements UserDetails
     */
    @Override
    public ExtendedUserDetails getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof UserDetails)) {
            throw new NotAuthenticatedException();
        }

        return (ExtendedUserDetails) principal;
    }

    /**
     * Get current user details
     *
     * @return ExtendedUserDetails user details with id or null if user not authenticated
     */
    @Override
    public ExtendedUserDetails getCurrentUserSafe() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof UserDetails)) {
            return null;
        }

        return (ExtendedUserDetails) principal;
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
    public boolean isUserExist(String email, String... extraEmails) {
        if (extraEmails.length == 0) {
            return userRepository.existsByEmail(email);
        }

        Set<String> emailSet = Arrays.stream(extraEmails).collect(Collectors.toSet());

        emailSet.add(email);

        return userRepository.existsByEmails(emailSet);
    }

    @Override
    public boolean isUserExist(Long id, Long... extraIds) {
        if (extraIds.length == 0) {
            return userRepository.existsById(id);
        }

        Set<Long> idSet = Arrays.stream(extraIds).collect(Collectors.toSet());

        idSet.add(id);

        return userRepository.existsByIds(idSet);
    }
}
