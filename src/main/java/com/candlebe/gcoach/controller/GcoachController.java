package com.candlebe.gcoach.controller;

import com.candlebe.gcoach.dto.MemberDTO;
import com.candlebe.gcoach.service.JoinService;
import com.candlebe.gcoach.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

@Controller
@Log4j2
@RequiredArgsConstructor
public class GcoachController {

    private final MemberService memberService;
    private final JoinService joinService;

    @GetMapping("/test")
    public void testPage(){
        log.info("testPage..........");
    }

    @GetMapping("/login")
    public void login() {
        log.info("login..........");
    }

    @PostMapping("/login")
    public void postLogin() {
        log.info("postLogin..........");
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
        log.info("----------닉네임 중복 검사----------");
        log.info("닉네임 : " + nickname);
        String result = memberService.checkNickname(nickname);
        log.info("닉네임 중복 여부 : " + result);
        log.info("---------------------------------");
        return result;
    }

    @GetMapping("/join")
    public String join(MemberDTO memberDTO) {
        log.info("join..........");
        return "/join";
    }

    @PostMapping("/join")
    public String join(@Valid MemberDTO memberDTO, Errors errors, Model model, RedirectAttributes redirectAttributes) {
        log.info("----------회원가입----------");
        log.info("아이디 : " + memberDTO.getUsername());
        log.info("비밀번호 : " + memberDTO.getPassword());
        log.info("비밀번호 확인 : " + memberDTO.getConfirmPassword());
        log.info("이름 : " + memberDTO.getName());
        log.info("휴대폰 : " + memberDTO.getPhone());

        if (errors.hasErrors()) {
            // 회원가입 실패시, 입력 데이터를 유지
            model.addAttribute("memberDTO", memberDTO);

            log.info("-----유효성 검사 오류 종류-----");
            for (FieldError error : errors.getFieldErrors()) {
                log.info(String.format("valid_%s", error.getField()) + " : " + error.getDefaultMessage());
            }
            log.info("----------------------------");

            return "/join";
        }

        String result = joinService.join(memberDTO);
        if (result.equals("fail")) {
            model.addAttribute("memberDTO", memberDTO);
            model.addAttribute("msg", memberDTO.getUsername() + "은 이미 존재하는 아이디입니다.");
            return "/join";
        } else if (result.equals("success")) {
            redirectAttributes.addFlashAttribute("msg", "회원가입 성공");
            return "redirect:/login";
        }
        return "redirect:/login";
    }
}
