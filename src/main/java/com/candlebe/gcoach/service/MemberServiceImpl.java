package com.candlebe.gcoach.service;

import com.candlebe.gcoach.dto.MemberDTO;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.MemberRepository;
import com.candlebe.gcoach.security.dto.AuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public String checkNickname(String nickname) {
        Optional<Member> result =  memberRepository.findMemberByNickname(nickname);
        if (result.isEmpty()) {
            return "success";
        } else {
            return "fail";
        }
    }

    public MemberDTO authMemberDtoToMemberDto(@AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        try {
            Member member = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial()).orElseThrow();
            return MemberDTO.builder()
                    .username(member.getUsername())
                    .password(member.getPassword())
                    .nickname(member.getNickname())
                    .name(member.getName())
                    .phone(member.getPhone())
                    .formSocial(member.isFormSocial())
                    .socialType(member.getSocialType())
                    .interest(member.getInterest())
                    .emotion(member.getEmotion())
                    .checkLogin(true)
                    .build();
        } catch (Exception e) {
            return MemberDTO.builder().build();
        }

    }
}
