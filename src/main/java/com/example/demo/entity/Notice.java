package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String nTitle;

    @Column
    private String nContent;

    private LocalDateTime created_at;
    private LocalDateTime modified_at;

    public Notice(String title, String content) {
        this.nTitle = title;
        this.nContent = content;
    }

    @PrePersist
    public void prePersist() {
        this.created_at = LocalDateTime.now();
        this.modified_at = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.modified_at = LocalDateTime.now();
    }

    public void update(String title, String nContent) {
        this.nTitle=  title;
        this.nContent = nContent;
    }

}
