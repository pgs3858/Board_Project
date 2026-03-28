package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record NoticeUpdateReq(
        @NotBlank String title,
        @NotBlank String content
) {
}
