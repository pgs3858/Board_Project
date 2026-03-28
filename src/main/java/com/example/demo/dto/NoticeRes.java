package com.example.demo.dto;

import com.example.demo.entity.Notice;

import java.time.LocalDateTime;

public record NoticeRes(
        int id,
        String title,
        String content,
        LocalDateTime created_at,
        LocalDateTime modified_at
) {
    public static NoticeRes from(Notice notice) {
        return new NoticeRes(
                notice.getId(),
            notice.getNTitle(),
                notice.getNContent(),
                notice.getCreated_at(),
                notice.getModified_at()
        );
    }
}
