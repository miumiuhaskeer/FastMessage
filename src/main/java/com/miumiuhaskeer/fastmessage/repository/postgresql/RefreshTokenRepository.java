package com.miumiuhaskeer.fastmessage.repository.postgresql;

import com.miumiuhaskeer.fastmessage.model.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Override
    Optional<RefreshToken> findById(Long id);
    Optional<RefreshToken> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
