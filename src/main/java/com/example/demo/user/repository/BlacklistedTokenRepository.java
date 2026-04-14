package com.example.demo.user.repository;

import com.example.demo.user.entity.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistToken ,Long> {

    // 블랙리리스에 토큰이 있는지 확인하려고 만든 메서드
    boolean existsByToken(String token);

    @Modifying
    @Query("DELETE FROM BlacklistToken b WHERE b.expiresAt < :now")
    public void deleteExpiredTokens(LocalDateTime now);
}
