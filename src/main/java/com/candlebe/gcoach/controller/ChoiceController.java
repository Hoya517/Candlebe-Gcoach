package com.candlebe.gcoach.controller;

import com.candlebe.gcoach.dto.EmotionDTO;
import com.candlebe.gcoach.dto.InterestDTO;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.MemberRepository;
import com.candlebe.gcoach.security.dto.AuthMemberDTO;
import com.candlebe.gcoach.service.ChoiceEmotionService;
import com.candlebe.gcoach.service.ChoiceInterestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@Log4j2
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class ChoiceController {

    private final ChoiceInterestService choiceInterestService;
    private final ChoiceEmotionService choiceEmotionService;
    private final MemberRepository memberRepository;

    @GetMapping("/choice/interest")
    public String createInterestForm(Model model, @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        log.info("choice_interest Page..........");
        Member member = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial()).orElseThrow();
        boolean result = choiceInterestService.isInterest(member.getUsername());

        if(result) {
            return "redirect:/";
        } else {
            model.addAttribute("interestDTD", new InterestDTO());
            return "choice_interest";
        }
    }

    @PostMapping("/choice/interest")
    public String choiceInterest(InterestDTO interestDTO, @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        Member member = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial()).orElseThrow();

        choiceInterestService.choiceInterest(member.getUsername(), interestDTO.getInterest());
        return "redirect:/choice/emotion";
    }

    @GetMapping("/choice/emotion")
    public String createEmotionForm(Model model, @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        log.info("choice_emotion Page..........");
        Member member = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial()).orElseThrow();
        boolean result = choiceEmotionService.isEmotion(member.getUsername());

        if(result) {
            return "redirect:/";
        } else {
            model.addAttribute("emotionDTO", new EmotionDTO());
            return "choice_emotion";
        }
    }

    @PostMapping("/choice/emotion")
    public String choiceEmotion(EmotionDTO emotionDTO, @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        Member member = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial()).orElseThrow();

        choiceEmotionService.choiceEmotion(member.getUsername(), emotionDTO.getEmotion());
        return "redirect:/";
    }
}
