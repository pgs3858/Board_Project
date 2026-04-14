package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.View;

import java.util.HashMap;
import java.util.Map;

/**
 * 전역 예외 처리기
 *
 * @RestControllerAdvice:
 *   모든 컨트롤러에서 발생하는 예외를 한 곳에서 처리
 *   각 컨트롤러마다 try-catch 쓰지 않아도 됨
 *
 * 처리하는 예외 종류:
 *   1. CustomException       → 비즈니스 로직 예외 (중복 아이디, 잘못된 비밀번호 등)
 *   2. MethodArgumentNotValidException → @Valid 유효성 검사 실패
 */
@RestControllerAdvice
public class GlobalException {



    /**
     * CustomException 처리
     * 응답 예시: { "message": "이미 사용 중인 아이디입니다." }
     */

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String,String>> handleCustomException(CustomException e){
        Map<String,String> error = new HashMap<>();
        error.put("message", e.getMessage());

        //CustomException 에 담긴 HttpStatus를 그대로 응답 코드로 사용
        return ResponseEntity.status(e.getStatus()).body(error);
    }
    /**
     * @Valid 유효성 검사 실패 처리
     * 응답 예시: { "loginId": "아이디를 입력해주세요.", "email": "이메일 형식이 올바르지 않습니다." }
     * 여러 필드가 동시에 실패할 수 있으므로 Map으로 모두 담아서 반환
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);  // 400 Bad Request
    }


}
