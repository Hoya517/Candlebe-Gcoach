package com.candlebe.gcoach.controller;

import com.candlebe.gcoach.entity.*;
import com.candlebe.gcoach.repository.*;
import com.candlebe.gcoach.security.dto.AuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class SettingController {

    private final MemberRepository memberRepository;
    private final HistoryRepository historyRepository;
    private final LikeRepository likeRepository;
    private final ReplyRepository replyRepository;
    private final DiaryRepository diaryRepository;

    @GetMapping("setting")
    public String getSetting() {
        return "setting";
    }

    @GetMapping("/deleteAccount")
    public String getDeleteAccount() {
        return "deleteAccount";
    }

    @PostMapping("/deleteAccount")
    public String postDeleteAccount(@AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        Member member = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial()).orElseThrow();
        historyRepository.deleteHistories(member);
        likeRepository.deleteLikes(member);
        replyRepository.deleteReplies(member);
        diaryRepository.deleteDiaries(member);
        member.getRoleSet().clear();
        memberRepository.save(member);
        memberRepository.deleteMember(member.getMid());
        return "redirect:/login";
    }
}