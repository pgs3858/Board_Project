package com.example.demo.dto;

import java.util.List;

public record NoticeList<T>(
        List<T> products
) {
}
