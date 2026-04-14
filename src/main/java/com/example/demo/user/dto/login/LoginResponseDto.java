package com.example.demo.user.dto.login;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {

    /*응답 DTO
            로그인하면  토큰 발급*/

    private String token; // JWP토큰
    private String loginId;
    private String email;
    private String userName;
    private String role;
    



}
