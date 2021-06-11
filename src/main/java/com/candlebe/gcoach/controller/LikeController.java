package com.candlebe.gcoach.controller;

import com.candlebe.gcoach.dto.PlayDTO;
import com.candlebe.gcoach.entity.Content;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.ContentRepository;
import com.candlebe.gcoach.repository.LikeRepository;
import com.candlebe.gcoach.repository.MemberRepository;
import com.candlebe.gcoach.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Log4j2
@RequiredArgsConstructor
public class LikeController {

    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    private final LikeRepository likeRepository;
    private final LikeService likeService;

    @PostMapping("/like")
    public String addLike(@RequestBody PlayDTO dto) {
        log.info("----------좋아요----------");
        log.info("dto : " + dto);
        Optional<Member> members = memberRepository.findById(dto.getMid());
        Optional<Content> contents = contentRepository.findById(dto.getCid());
        if (members.isPresent() && contents.isPresent()) {
            Member member = members.get();
            Content content = contents.get();
            boolean likeCheck = likeService.addLike(member.getMid(), content.getCid());
            int likeCount = likeRepository.likeCount(content);
            return "{\"likeCheck\":"+ likeCheck + ",\"likeCount\":" + likeCount + "}";
        }
        return "redirect:/logout";
    }
}
