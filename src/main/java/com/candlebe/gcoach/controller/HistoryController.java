package com.candlebe.gcoach.controller;

import com.candlebe.gcoach.entity.History;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.HistoryRepository;
import com.candlebe.gcoach.repository.MemberRepository;
import com.candlebe.gcoach.security.dto.AuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
public class HistoryController {

    private final MemberRepository memberRepository;
    private final HistoryRepository historyRepository;

    @GetMapping("/history")
    public String getHistory(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, Model model) {
        log.info("getHistory..........");
        Member member = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial()).orElseThrow();
        List<History> histories = historyRepository.getHistoryWithAll(member);
        model.addAttribute("histories", histories);
        return "history";
    }
}
