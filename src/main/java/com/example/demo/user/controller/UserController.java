package com.example.demo.user.controller;

import com.example.demo.user.dto.login.LoginRequestDto;
import com.example.demo.user.dto.login.LoginResponseDto;
import com.example.demo.user.dto.signup.SignupRequestDto;
import com.example.demo.user.dto.signup.SignupResponseDto;
import com.example.demo.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;



    /**
     * 회원가입 API
     * POST /api/users/signup
     *
     * @Valid: SignupRequestDto의 @NotBlank, @Email, @Size 등 자동 검사
     *         유효성 실패 시 GlobalExceptionHandler로 넘어감
     * @RequestBody: HTTP Body의 JSON을 SignupRequestDto로 변환
     * ResponseEntity.ok(): HTTP 200 + body
     */
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto dto) {
        System.out.println("회원가입");
        return ResponseEntity.ok(userService.signup(dto));
    }



    /**
     * 로그인 API
     * POST /api/users/login
     *
     * 로그인 성공 시 JWT 토큰 포함된 LoginResponseDto 반환
     * 클라이언트는 이후 요청 시 헤더에 토큰을 담아야 함
     * Authorization: Bearer {token}
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(userService.login(dto));
    }
    /**
     * 로그아웃 API
     * POST /api/users/logout
     *
     * @RequestHeader: HTTP 헤더에서 Authorization 값 꺼냄
     *   클라이언트가 "Authorization: Bearer eyJ..." 형태로 보내야 함
     *
     * 로그아웃 후 클라이언트도 저장된 토큰을 삭제해야 함
     * (localStorage, cookie 등에서 제거)
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(
            @RequestHeader("Authorization") String bearerToken) {
        userService.logout(bearerToken);
        Map<String, String> response = new HashMap<>();
        response.put("message", "로그아웃 되었습니다.");
        return ResponseEntity.ok(response);
    }

}