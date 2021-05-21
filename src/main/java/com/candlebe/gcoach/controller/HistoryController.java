package com.candlebe.gcoach.controller;

import com.candlebe.gcoach.dto.MemberDTO;
import com.candlebe.gcoach.entity.History;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.HistoryRepository;
import com.candlebe.gcoach.repository.MemberRepository;
import com.candlebe.gcoach.security.dto.AuthMemberDTO;
import com.candlebe.gcoach.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class HistoryController {

    private final MemberRepository memberRepository;
    private final HistoryRepository historyRepository;
    private final MemberService memberService;

    @GetMapping("/history")
    public String getHistory(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, Model model) {
        log.info("getHistory..........");
        MemberDTO memberDTO = memberService.authMemberDtoToMemberDto(authMemberDTO);
        model.addAttribute("memberDTO", memberDTO);
        Member member = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial()).orElseThrow();
        List<History> histories = historyRepository.getHistoryWithAll(member);
        model.addAttribute("histories", histories);
        return "history";
    }
}
