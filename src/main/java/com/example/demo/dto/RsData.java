package com.example.demo.dto;

import com.example.demo.entity.Notice;

import java.util.List;

public record RsData<T>(
        String code,
        T data,
        String msg
) {

    public static <T> RsData<T> ok(T data) {
        return new RsData<>("200",data,"조회에 성공했어요!");
    }

    public static RsData<Void> ok() {
        return new RsData<>("200",null,"성공했어요");
    }

    public static RsData<NoticeRes> Writeok(NoticeRes noticeRes) {
        return new RsData<>("200",noticeRes,"성공했어요");
    }
}
