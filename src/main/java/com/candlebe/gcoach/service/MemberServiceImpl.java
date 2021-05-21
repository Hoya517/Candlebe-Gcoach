package com.candlebe.gcoach.service;

import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

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
}
