package com.example.demo.user.dto.signup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


/**
 * 회원가입 요청 DTO (Data Transfer Object)
 *
 * DTO를 사용하는 이유:
 *   - Entity를 직접 받으면 불필요한 필드(id, role 등)까지 노출됨
 *   - 요청/응답에 필요한 데이터만 정의해서 명확하게 분리
 *
 * @Valid 동작 방식:
 *   컨트롤러에서 @Valid 선언 시 아래 어노테이션들이 자동으로 검사됨
 *   실패 시 GlobalExceptionHandler의 MethodArgumentNotValidException 처리
 */
@Getter

public class SignupRequestDto {

    /**
     * 로그인 아이디
     * @NotBlank: null, 빈 문자열(""), 공백(" ") 모두 거부
     * @Size: 4~20자 제한
     */
    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min = 4, max = 20, message = "아이디는 4~20자 사이여야 합니다.")
    private String loginId;

    /**
     * 이메일
     * @Email: "문자@문자.문자" 형식 검사
     */
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    /**
     * 비밀번호
     * 최소 8자 강제. 추후 정규식(@Pattern)으로 특수문자 포함 등 강화 가능
     */
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;

    /**
     * 사용자 이름 (닉네임)
     */
    @NotBlank(message = "이름을 입력해주세요.")
    private String username;


}
