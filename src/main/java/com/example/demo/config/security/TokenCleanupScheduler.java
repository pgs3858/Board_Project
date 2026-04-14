package com.example.demo.config.security;

import com.example.demo.user.repository.BlacklistedTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 만료된 블랙리스트 토큰 자동 삭제 스케줄러
 * 만료된 토큰은 어차피 validateToken()에서 걸러지므로 DB만 정리하는 용도
 *
 * 사용하려면 DemoApplication.java에 @EnableScheduling 추가 필요
 */
@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {

    private final BlacklistedTokenRepository blacklistedTokenRepository;

    /**
     * 매일 새벽 2시에 만료된 토큰 삭제
     * cron = "초 분 시 일 월 요일"
     */
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void cleanupExpiredTokens() {
        blacklistedTokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }
}