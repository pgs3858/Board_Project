package com.example.demo.user.entity;



import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNumber;



    @Column(unique = true )
    private String loginId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false ,unique = true)
    private String email;

    private LocalDate birthDay;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;





    @Builder
    public User( String loginId , String userName, String password, String email, LocalDate birthDay , Role role ) {

        this.loginId = loginId;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.birthDay=birthDay;
        this.role = role;


    }








}
