package com.candlebe.gcoach.security.handler;

import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.MemberRepository;
import com.candlebe.gcoach.security.dto.AuthMemberDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Log4j2
// 로그인 성공 이후 처리
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

        private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

        private final PasswordEncoder passwordEncoder;

        @Autowired
        private MemberRepository memberRepository;

    public LoginSuccessHandler(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        log.info("----------로그인 성공 이후 처리----------");
        log.info("onAuthenticationSuccess");

        AuthMemberDTO authMemberDTO = (AuthMemberDTO) authentication.getPrincipal();
        boolean formSocial = authMemberDTO.isFormSocial();
        log.info("password : " + authMemberDTO.getPassword());
        log.info("Need Modify Member? : " + formSocial);

        Optional<Member> result = memberRepository.findByUsername(authMemberDTO.getUsername(), formSocial);
        result.ifPresent(log::info);

        // 로그인한 사용자의 비밀번호가 1111 이라면 true
        //boolean passwordResult = passwordEncoder.matches("1111", authMemberDTO.getPassword());

        // 소셜 로그인 사용자이고 비밀번호가 1111 이라면 회원 정보 변경 사이트(/sample/modify?form=social) 로 리다이렉트
//        if (formSocial && passwordResult) {
//            redirectStrategy.sendRedirect(request, response, "/sample/modify?form=social");
//        }
    }
}
