package com.example.demo.controller;


import com.example.demo.dto.*;
import com.example.demo.entity.Notice;
import com.example.demo.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항 전체 조회")
    @GetMapping
    public RsData<NoticeList<NoticeRes>> list() {

        List<Notice> noticeList = noticeService.list();

        List<NoticeRes> ResList = noticeList.stream()
                .map(NoticeRes::from)
                .toList();

        return RsData.ok(new NoticeList<>(ResList));

    }

    @Operation(summary = "공지사항 작성")
    @PostMapping
    public RsData<NoticeRes> write(@RequestBody @Valid NoticeReq req) {
        Notice notice = this.noticeService.write(req.title(), req.content());
        return RsData.Writeok(NoticeRes.from(notice));
    }

    @Operation(summary = "공지사항 삭제")
    @DeleteMapping("/{id}")
    public RsData<Void> delete(@PathVariable int id) {
        //Todo 해당 req가 존재하지 않을 경우

        this.noticeService.delete(id);
        return RsData.ok();
    }


    @Operation(summary = "공지사항 수정")
    @PutMapping("/{id}")
    public RsData<NoticeRes> update(@PathVariable int id,
            @RequestBody @Valid NoticeUpdateReq req) {
        //Todo 해당 req가 존재하지 않을 경우예외
        Notice notice = this.noticeService.update(id,req);
        return RsData.ok(NoticeRes.from(notice));
    }
}
