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
public class ChoiceController {

    private final ChoiceInterestService choiceInterestService;
    private final ChoiceEmotionService choiceEmotionService;
    private final MemberRepository memberRepository;

    @GetMapping("/choice/interest")
    public String createInterestForm(Model model, @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        log.info("choice_interest Page..........");
        Optional<Member> members = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial());
        if (members.isPresent()) {
            Member member = members.get();
            boolean result = choiceInterestService.isInterest(member.getUsername());

            if(result) {
                return "redirect:/";
            } else {
                model.addAttribute("interestDTD", new InterestDTO());
                return "choice_interest";
            }
        }
        return "redirect:/logout";
    }

    @PostMapping("/choice/interest")
    public String choiceInterest(InterestDTO interestDTO, @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        Optional<Member> members = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial());
        if (members.isPresent()) {
            Member member = members.get();
            choiceInterestService.choiceInterest(member.getUsername(), interestDTO.getInterest());
            return "redirect:/choice/emotion";
        }
        return "login";
    }

    @GetMapping("/choice/emotion")
    public String createEmotionForm(Model model, @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        log.info("choice_emotion Page..........");
        Optional<Member> members = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial());
        if (members.isPresent()) {
            Member member = members.get();
            boolean result = choiceEmotionService.isEmotion(member.getUsername());

            if(result) {
                return "redirect:/";
            } else {
                model.addAttribute("emotionDTO", new EmotionDTO());
                return "choice_emotion";
            }
        }
        return "redirect:/logout";
    }

    @PostMapping("/choice/emotion")
    public String choiceEmotion(EmotionDTO emotionDTO, @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        Optional<Member> members = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial());
        if (members.isPresent()) {
            Member member = members.get();
            choiceEmotionService.choiceEmotion(member.getUsername(), emotionDTO.getEmotion());
            return "redirect:/";
        }
        return "redirect:/logout";
    }
}
