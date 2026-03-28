package com.example.demo.service;

import com.example.demo.dto.NoticeUpdateReq;
import com.example.demo.entity.Notice;
import com.example.demo.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public List<Notice> list() {
        return this.noticeRepository.findAll();
    }

    public Notice write(String title, String content) {
        Notice notice = new Notice(title, content);
        return this.noticeRepository.save(notice);
    }

    public void delete(int id) {
//        Optional<Notice> notice = this.noticeRepository.findById(id);
//        notice.ifPresent((n)->
//        {
//           this.noticeRepository.delete(n);
//        });
        //완전 간결하게 사용
        this.noticeRepository.findById(id).
                ifPresent(this.noticeRepository::delete);
    }

    public Notice update(int id, NoticeUpdateReq req) {
        Notice notice = this.noticeRepository.findById(id).get();
        notice.update(req.title(), req.content());
        return notice;
    }
}
