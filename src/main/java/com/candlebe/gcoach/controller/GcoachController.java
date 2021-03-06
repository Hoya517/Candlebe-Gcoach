package com.candlebe.gcoach.controller;

import com.candlebe.gcoach.dto.MemberDTO;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.entity.MemberRole;
import com.candlebe.gcoach.repository.MemberRepository;
import com.candlebe.gcoach.security.dto.AuthMemberDTO;
import com.candlebe.gcoach.service.JoinService;
import com.candlebe.gcoach.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@Log4j2
@RequiredArgsConstructor
public class GcoachController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final JoinService joinService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        try {
            Optional<Member> members = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial());
            if (members.isPresent()) {
                return "redirect:/";
            }
        } catch (Exception e) {
            return "login";
        }
        return "login";
    }

    @PostMapping("/login")
    public String postLogin() {
        return "login";
    }

    @RequestMapping("/checkUsername")
    @ResponseBody
    public String checkUsername(MemberDTO memberDTO) {
        log.info("checkUsername : " + memberDTO.getUsername());
        String result = joinService.checkUsername(memberDTO);
        log.info(result);
        return result;
    }

    @RequestMapping("/checkNickname")
    @ResponseBody
    public String checkNickname(String nickname) {
        log.info("----------????????? ?????? ??????----------");
        log.info("????????? : " + nickname);
        String result = memberService.checkNickname(nickname);
        log.info("????????? ?????? ?????? : " + result);
        log.info("---------------------------------");
        return result;
    }

    @GetMapping("/join")
    public String join(MemberDTO memberDTO) {
        log.info("join..........");
        return "join";
    }

    @PostMapping("/join")
    public String join(@Valid MemberDTO memberDTO, Errors errors, Model model, RedirectAttributes redirectAttributes) {
        log.info("----------????????????----------");
        log.info("????????? : " + memberDTO.getUsername());
        log.info("???????????? : " + memberDTO.getPassword());
        log.info("???????????? ?????? : " + memberDTO.getConfirmPassword());
        log.info("?????? : " + memberDTO.getName());
        log.info("????????? : " + memberDTO.getNickname());
        log.info("????????? : " + memberDTO.getPhone());

        if (errors.hasErrors()) {
            // ???????????? ?????????, ?????? ???????????? ??????
            model.addAttribute("memberDTO", memberDTO);

            log.info("-----????????? ?????? ?????? ??????-----");
            for (FieldError error : errors.getFieldErrors()) {
                log.info(String.format("valid_%s", error.getField()) + " : " + error.getDefaultMessage());
            }
            log.info("----------------------------");

            return "join";
        }

        String result = joinService.join(memberDTO);
        if (result.equals("fail")) {
            model.addAttribute("memberDTO", memberDTO);
            model.addAttribute("msg", memberDTO.getUsername() + "??? ?????? ???????????? ??????????????????.");
            return "/join";
        } else if (result.equals("success")) {
            redirectAttributes.addFlashAttribute("msg", "???????????? ??????");
            return "redirect:/login";
        }
        return "redirect:/login";
    }

    @GetMapping("/createAdmin")
    public String createAdmin() {
        Member member = Member.builder()
                .username("admin")
                .password(passwordEncoder.encode("1111"))
                .name("?????????1")
                .nickname("?????????1")
                .phone("01012345678")
                .build();
        member.addMemberRole(MemberRole.ADMIN);
        memberRepository.save(member);
        return "redirect:/login";
    }
}
