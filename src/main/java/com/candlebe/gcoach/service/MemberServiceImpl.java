package com.candlebe.gcoach.service;

import com.candlebe.gcoach.dto.MemberDTO;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.entity.MemberRole;
import com.candlebe.gcoach.repository.MemberRepository;
import com.candlebe.gcoach.security.dto.AuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public String checkNickname(String nickname) {
        Optional<Member> result =  memberRepository.findMemberByNickname(nickname);
        if (result.isPresent()) {
            return "fail";
        } else {
            return "success";
        }
    }

    public MemberDTO authMemberDtoToMemberDto(@AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        try {
            Optional<Member> members = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial());
            if (members.isPresent()) {
                Member member = members.get();
                MemberDTO memberDTO = MemberDTO.builder()
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

                Iterator<MemberRole> it = member.getRoleSet().iterator();
                if(it.hasNext()) {
                    if (it.next().toString().equals("ADMIN")) {
                        memberDTO.setAdmin(true);
                    }
                }
                return memberDTO;
            }
            return MemberDTO.builder().build();
        } catch (Exception e) {
            return MemberDTO.builder().build();
        }
    }
}
