package com.example.demo.user.service;

import com.example.demo.exception.CustomException;
import com.example.demo.config.security.JwtTokenProvider;
import com.example.demo.user.dto.login.LoginRequestDto;
import com.example.demo.user.dto.login.LoginResponseDto;
import com.example.demo.user.dto.signup.SignupRequestDto;
import com.example.demo.user.dto.signup.SignupResponseDto;
import com.example.demo.user.entity.BlacklistToken;
import com.example.demo.user.entity.Role;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.BlacklistedTokenRepository;
import com.example.demo.user.repository.RoleRepository;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원 비즈니스 로직 담당
 *
 * @Service: 스프링 빈으로 등록
 * @RequiredArgsConstructor: final 필드를 생성자 주입으로 자동 처리
 * @Transactional: DB 작업 중 예외 발생 시 자동 롤백
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;  // SecurityConfig에서 @Bean으로 등록한 BCryptPasswordEncoder
    private final JwtTokenProvider jwtTokenProvider;
    private final BlacklistedTokenRepository blacklistedTokenRepository;


    /** 회원가입
     * 처리 순서:
     *   1. 아이디 중복 체크
     *   2. 이메일 중복 체크
     *   3. roles 테이블에서 ROLE_USER 조회
     *   4. 비밀번호 BCrypt 암호화
     *   5. User 저장 (role FK 연결)
     *   6. 응답 DTO 반환
     */
    public SignupResponseDto signup(SignupRequestDto dto) {

        // 1. 아이디 중복 체크

        if (userRepository.existsByLoginId(dto.getLoginId())) {
            throw new CustomException("이미 사용 중인 아이디입니다.", HttpStatus.CONFLICT);  // 409
        }

        // 2. 이메일 중복 체크
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new CustomException("이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT);  // 409
        }

        // 3. roles 테이블에서 ROLE_USER 조회
        // data.sql에서 미리 INSERT 해둔 값
        Role role = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() ->
                        new CustomException("기본 권한을 찾을 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR));

        // 4. 비밀번호 암호화 + User 엔티티 생성
        // passwordEncoder.encode("password123") → "$2a$10$..."
        User user = User.builder()
                .loginId(dto.getLoginId())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))  // 암호화된 값 저장
                .userName(dto.getUsername())
                .role(role)
                .build();


        // 5. DB 저장
        // INSERT INTO users (login_id, email, password, username, role_id) VALUES (...)
        User saved = userRepository.save(user);

        // 6. 응답 (비밀번호 제외)
        return new SignupResponseDto(
                saved.getUserNumber(),
                saved.getLoginId(),
                saved.getEmail(),
                saved.getUserName(),
                saved.getRole().getRoleName()  // "ROLE_USER"



        );


    }


    /**
     * 로그인
     *
     * 처리 순서:
     *   1. 아이디로 유저 조회
     *   2. BCrypt 비밀번호 비교
     *   3. JWT 토큰 발급 후 반환
     */
    public LoginResponseDto login(LoginRequestDto dto) {

        // 1. 아이디로 유저 조회
        // SELECT * FROM users WHERE login_id = ?
        User user = userRepository.findByLoginId(dto.getLoginId())
                .orElseThrow(() ->
                        new CustomException("존재하지 않는 아이디입니다.", HttpStatus.NOT_FOUND));  // 404

        // 2. 비밀번호 비교
        // passwordEncoder.matches("입력한 평문", "DB의 암호화된 값")
        // BCrypt는 동일 비밀번호여도 해시가 달라서 equals() 비교 불가 → matches() 사용
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CustomException("비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED);  // 401
        }

        // 3. JWT 토큰 발급
        // loginId를 subject로 담아 서명된 JWT 생성
        String token = jwtTokenProvider.generateToken(user.getLoginId());

        return new LoginResponseDto(
                token,
                user.getLoginId(),
                user.getEmail(),
                user.getUserName(),
                user.getRole().getRoleName()
        );



    }
    public void logout(String bearerToken) {
        // "Bearer eyJ..." → "eyJ..." 추출
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new CustomException("토큰이 없습니다.", HttpStatus.BAD_REQUEST);
        }
        String token = bearerToken.substring(7);

        // 이미 유효하지 않은 토큰이면 로그아웃 불필요
        if (!jwtTokenProvider.validateToken(token)) {
            throw new CustomException("이미 만료된 토큰입니다.", HttpStatus.BAD_REQUEST);
        }

        // 이미 블랙리스트에 있으면 (이미 로그아웃된 상태)
        if (blacklistedTokenRepository.existsByToken(token)) {
            throw new CustomException("이미 로그아웃된 토큰입니다.", HttpStatus.BAD_REQUEST);
        }

        // 블랙리스트에 저장
        blacklistedTokenRepository.save(
                BlacklistToken.builder()
                        .token(token)
                        .expiresAt(jwtTokenProvider.getExpirationFromToken(token)) // 토큰 만료 시간 저장
                        .build()
        );
    }




}