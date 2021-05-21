package com.candlebe.gcoach.controller;

import com.candlebe.gcoach.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequiredArgsConstructor
public class AxiosController {

    private final MemberRepository memberRepository;

    @ResponseBody
    @RequestMapping("/loginTest")
    public String postLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
        log.info("loginTest..........");
        log.info(username);
        log.info(password);

        return "success";
    }
}
