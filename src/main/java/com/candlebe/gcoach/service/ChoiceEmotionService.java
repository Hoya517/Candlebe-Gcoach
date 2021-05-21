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
public class ChoiceEmotionService {

    private final MemberRepository memberRepository;

    @Transactional
    public void choiceEmotion(String userName, String emotion) {
        Optional<Member> result = memberRepository.findByUsername(userName);
        Member member = result.get();

        member.setEmotion(emotion);
    }

    public boolean isEmotion(String userName) {
        Optional<Member> result = memberRepository.findByUsername(userName);
        String emotion = result.get().getEmotion();
        if (emotion == "" || emotion == null) {
            return false;
        } else {
            return true;
        }
    }
}
