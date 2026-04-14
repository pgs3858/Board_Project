package com.example.demo.user.repository;

import com.example.demo.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    /**
     * 권한 이름으로 Role 조회
     * 메서드 이름 규칙: findBy + 필드명(Name) → JPA가 자동으로 쿼리 생성
     * 실행 SQL: SELECT * FROM roles WHERE name = ?
     *
     * 회원가입 시 "ROLE_USER"로 조회해서 User에 연결
     * Optional: 해당 name의 Role이 없을 수도 있으므로 Optional로 감쌈
     */
    Optional<Role> findByRoleName(String roleName);
}
