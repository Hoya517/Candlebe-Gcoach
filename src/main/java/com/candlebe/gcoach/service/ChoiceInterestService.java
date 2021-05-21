package com.candlebe.gcoach.service;

import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class ChoiceInterestService {

    private final MemberRepository memberRepository;

    @Transactional
    public void choiceInterest(String userName, String interest) {
        Optional<Member> result = memberRepository.findByUsername(userName);
        Member member = result.get();

        member.setInterest(interest);
    }

    public boolean isInterest(String userName) {
        Optional<Member> result = memberRepository.findByUsername(userName);
        String interest = result.get().getInterest();
        if (interest == "" || interest == null) {
            return false;
        } else {
            return true;
        }
    }

}
