package com.example.demo.config.security;




import com.example.demo.user.repository.BlacklistedTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 전체 설정
 *
 * @Configuration: 스프링 설정 클래스
 * @EnableWebSecurity: Spring Security 활성화
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * BCryptPasswordEncoder 빈 등록
     *
     * BCrypt 특징:
     *   - 같은 비밀번호도 매번 다른 해시값 생성 (Salt 자동 적용)
     *   - passwordEncoder.matches("원문", "해시값") 으로 비교
     *   - 단방향 암호화 → 해시값으로 원문 복원 불가
     *
     * @Bean으로 등록해야 UserService에서 @RequiredArgsConstructor로 주입 가능
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Security 필터 체인 설정
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtTokenProvider jwtTokenProvider, BlacklistedTokenRepository blacklistedTokenRepository) throws Exception {
        http
                // JWT 방식은 stateless → CSRF 토큰 불필요
                .csrf(csrf -> csrf.disable())

                // 세션 사용 안 함 (JWT가 세션 역할 대체)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // 아래 URL은 토큰 없이 허용 (회원가입, 로그인, H2 콘솔)
                        .requestMatchers(
                                "/api/users/signup",
                                "/api/users/login",
                                "/h2-console/**"
                        ).permitAll()
                        // 나머지 모든 요청은 유효한 JWT 토큰 필수
                        .anyRequest().authenticated()
                )

                // H2 콘솔은 iframe 사용 → frameOptions 비활성화
                .headers(headers ->
                        headers.frameOptions(frame -> frame.disable()))

                // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 등록
                // 요청이 들어오면 JWT 필터가 먼저 토큰을 검사하고 인증 처리
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider , blacklistedTokenRepository),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}