package com.miumiuhaskeer.fastmessage.controller;

import com.miumiuhaskeer.fastmessage.model.UserDetailsImpl;
import com.miumiuhaskeer.fastmessage.model.entity.RefreshToken;
import com.miumiuhaskeer.fastmessage.model.request.LoginRequest;
import com.miumiuhaskeer.fastmessage.model.request.RefreshTokenRequest;
import com.miumiuhaskeer.fastmessage.model.response.LoginResponse;
import com.miumiuhaskeer.fastmessage.model.response.RefreshTokenResponse;
import com.miumiuhaskeer.fastmessage.service.RefreshTokenService;
import com.miumiuhaskeer.fastmessage.service.UserService;
import com.miumiuhaskeer.fastmessage.util.JWTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthenticationController {

    private final UserService userService;
    private final JWTokenUtil jwTokenUtil;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> createAuthenticationToken(@RequestBody @Valid LoginRequest request) {
        UserDetailsImpl userDetails = userService.authenticate(request.getEmail(), request.getPassword());
        String accessToken = jwTokenUtil.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new LoginResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                roles,
                accessToken,
                refreshToken.getToken()
        ));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        String newAccessToken = refreshTokenService.refreshExpiredToken(request.getUserId(), request.getRefreshToken());

        return ResponseEntity.ok(new RefreshTokenResponse(newAccessToken, request.getRefreshToken()));
    }
}
