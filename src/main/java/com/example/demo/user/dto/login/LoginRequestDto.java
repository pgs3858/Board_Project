package com.example.demo.user.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@AllArgsConstructor
public class LoginRequestDto {

//    로그인 요청DTO
    @NotBlank(message = "아이디를 입력해주세여")
    private String loginId;
    // 비밀번호 (일반문자로 받아서 service에서 BCrypt 비교할예정)
    @NotBlank(message = "비밀번호를 입력해주세여")
    private String password;


}
