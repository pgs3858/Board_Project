package com.example.demo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 비즈니스 로직에서 발생하는 커스텀 예외
 *
 * 사용 방법:
 *   throw new CustomException("이미 사용 중인 아이디입니다.", HttpStatus.CONFLICT);
 *
 * 장점:
 *   - HttpStatus를 함께 담아서 던지면
 *     GlobalExceptionHandler에서 그대로 HTTP 상태코드로 응답 가능
 *   - 어디서 던져도 일관된 에러 응답 포맷 유지
 */
@Getter
public class CustomException extends RuntimeException {

    /**
     * HTTP 상태코드
     * ex) 409 CONFLICT, 404 NOT_FOUND, 401 UNAUTHORIZED
     */
    private final HttpStatus status;

    public CustomException(String message, HttpStatus status) {
        super(message);           // RuntimeException의 message로 전달
        this.status = status;
    }
}