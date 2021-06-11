package com.candlebe.gcoach.controller;

import com.candlebe.gcoach.dto.MemberDTO;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.*;
import com.candlebe.gcoach.security.dto.AuthMemberDTO;
import com.candlebe.gcoach.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
@Log4j2
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class SettingController {

    private final MemberRepository memberRepository;
    private final HistoryRepository historyRepository;
    private final LikeRepository likeRepository;
    private final ReplyRepository replyRepository;
    private final DiaryRepository diaryRepository;
    private final MemberService memberService;

    @GetMapping("/setting")
    public String getSetting(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, Model model) {
        MemberDTO memberDTO = memberService.authMemberDtoToMemberDto(authMemberDTO);
        model.addAttribute("memberDTO", memberDTO);
        return "setting";
    }

    @GetMapping("/deleteAccount")
    public String getDeleteAccount() {
        return "deleteAccount";
    }

    @PostMapping("/deleteAccount")
    public String postDeleteAccount(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, Model model) {
        Optional<Member> members = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial());
        if (members.isPresent()) {
            Member member = members.get();
            historyRepository.deleteHistories(member);
            likeRepository.deleteLikes(member);
            replyRepository.deleteReplies(member);
            diaryRepository.deleteDiaries(member);
            member.getRoleSet().clear();
            memberRepository.save(member);
            memberRepository.deleteMember(member.getMid());
            model.addAttribute("msg", "회원탈퇴 성공");
            return "redirect:/logout";
        }
        model.addAttribute("msg", "회원탈퇴 실패");
        return "redirect:/logout";
    }
}