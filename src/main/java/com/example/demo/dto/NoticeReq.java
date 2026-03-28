package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record NoticeReq(
        int id,

        @NotBlank(message = "제목은 필수 입니다.")
        String title,

        @NotBlank(message = "내용은 필수 입니다.")
        String content

) {
}
