package com.example.demo.user.dto.signup;


import lombok.*;



@Getter
@AllArgsConstructor
public class SignupResponseDto {



    /*회원가입 응답 DTO
    비밀번호는 절대 응답에 포함 안함
    클라이언트에 꼭 필여한 정보만 담아서 반환*/

    private Long userNumber;
    private String loginId;
    private String email;
    private String userName;
    private String role;


}
