package com.example.demo.user.repository;

import com.example.demo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 * User 테이블 접근 인터페이스
 * JPA 메서드 네이밍 규칙으로 자동 쿼리 생성
 */
public interface UserRepository extends JpaRepository<User,Long> {

    /**
     * 아이디로 유저 조회
     * 실행 SQL: SELECT * FROM users WHERE login_id = ?
     * 로그인 시 사용
     */
    Optional<User> findByLoginId(String loginId);

    /**
     * 아이디 존재 여부 확인
     * 실행 SQL: SELECT COUNT(*) > 0 FROM users WHERE login_id = ?
     * 회원가입 시 아이디 중복 체크에 사용
     * existsBy~ 는 boolean 반환
     */
    boolean existsByLoginId(String loginId);


    /**
     * 이메일 존재 여부 확인
     * 실행 SQL: SELECT COUNT(*) > 0 FROM users WHERE email = ?
     * 회원가입 시 이메일 중복 체크에 사용
     */

    boolean existsByEmail(String email);

}
