package com.example.demo.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "blacklisted_tokens")
public class BlacklistToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blacklistId;

    //블랙리스트에 등록된 토큰
    @Column(nullable = false ,length = 600)
    private String token;


    //토큰 만료시간
    @Column(nullable = false )
    private LocalDateTime expiresAt;



}
