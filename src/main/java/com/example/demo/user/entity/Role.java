package com.example.demo.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "roles")
@Getter
@NoArgsConstructor
public class Role {
    /**
     * 권한(Role) 엔티티
     * DB 테이블명: roles
     *
     * users 테이블과의 관계:
     *   roles (1) ──────< users (N)
     *   하나의 Role을 여러 User가 가질 수 있음 (ManyToOne)
     *   users 테이블에 role_id(FK) 컬럼으로 연결됨
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    /**
     * 권한 이름
     * Spring Security 규칙상 "ROLE_" 접두사 필수
     * ex) ROLE_USER, ROLE_ADMIN
     */
    @Column(nullable = false , unique = true)
    private String roleName;

    @Builder
    public Role(String roleName) {
        this.roleName = roleName;


    }



}
