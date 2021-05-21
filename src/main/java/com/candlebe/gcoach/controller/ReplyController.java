package com.candlebe.gcoach.controller;

import com.candlebe.gcoach.dto.ReplyDTO;
import com.candlebe.gcoach.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reply")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    // 댓글 등록 처리
    @PostMapping("/{cid}")
    public ResponseEntity<Long> addReply(@RequestBody ReplyDTO replyDTO) {
        log.info("----------댓글 등록 처리----------");
        log.info("replyDTO : " + replyDTO);

        Long rid = replyService.register(replyDTO);
        return new ResponseEntity<>(rid, HttpStatus.OK);
    }

    // 댓글 조회 처리
    @GetMapping("/{cid}/all")
    public ResponseEntity<List<ReplyDTO>> getList(@PathVariable("cid") Long cid) {
        log.info("----------" + cid + "번 콘텐츠 리뷰 조회----------");
        List<ReplyDTO> replyDTOList = replyService.getListOfReply(cid);
        return new ResponseEntity<>(replyDTOList, HttpStatus.OK);
    }
}
