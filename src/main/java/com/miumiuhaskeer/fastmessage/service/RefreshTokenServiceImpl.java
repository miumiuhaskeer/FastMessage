package com.miumiuhaskeer.fastmessage.service;

import com.miumiuhaskeer.fastmessage.exception.RefreshTokenExpiredException;
import com.miumiuhaskeer.fastmessage.model.entity.RefreshToken;
import com.miumiuhaskeer.fastmessage.properties.bundle.ErrorBundle;
import com.miumiuhaskeer.fastmessage.properties.config.RefreshTokenProp;
import com.miumiuhaskeer.fastmessage.repository.postgresql.RefreshTokenRepository;
import com.miumiuhaskeer.fastmessage.util.JWTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenProp refreshTokenProp;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTokenUtil jwTokenUtil;
    private final UserService userService;

    /**
     * Refresh jwt token by refreshToken
     *
     * @param userId user that need to update token
     * @param refreshToken is needed to update jwt token
     * @return new token
     * @throws EntityNotFoundException if refresh token for user not found
     * @throws RefreshTokenExpiredException if refresh token expired
     * @throws UsernameNotFoundException if the user could not be found or the user has no GrantedAuthority
     */
    @Override
    public String refreshExpiredToken(Long userId, String refreshToken) {
        return refreshTokenRepository.findByUserId(userId)
                .filter(token -> token.getToken().equals(refreshToken))
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> jwTokenUtil.generateToken(user.getEmail()))
                .orElseThrow(() ->
                        new EntityNotFoundException(ErrorBundle.get("error.entityNotFoundException.token.refresh.message"))
                );
    }

    /**
     * Create refresh token by userId
     *
     * @param userId user that need to update token
     * @return refresh token entity
     * @throws EntityNotFoundException if user not found
     */
    @Override
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken token = new RefreshToken();

        token.setUser(userService.getById(userId));
        token.setExpiryDateTime(LocalDateTime.now().plusSeconds(refreshTokenProp.getExpirationSeconds()));
        token.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(token);
    }

    /**
     * Verify refresh token expiration
     *
     * @param token refresh token
     * @return refresh token entity
     * @throws RefreshTokenExpiredException if refresh token expired
     */
    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDateTime().compareTo(LocalDateTime.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenExpiredException();
        }

        return token;
    }

    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
