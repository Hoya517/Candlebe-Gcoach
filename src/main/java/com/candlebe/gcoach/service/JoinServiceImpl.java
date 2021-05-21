package com.candlebe.gcoach.service;

import com.candlebe.gcoach.dto.MemberDTO;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class JoinServiceImpl implements JoinService {

    private final MemberRepository memberRepository;

    @Override
    public String checkUsername(MemberDTO memberDTO) {
        Optional<Member> result = memberRepository.findByUsername(memberDTO.getUsername(),false);

        if (result.isPresent()) {
            log.info(result.get());
            return "fail";
        }
        return "success";
    }

    @Override
    public String join(MemberDTO memberDTO) {
        log.info("memberDTO : " + memberDTO);

        Optional<Member> result = memberRepository.findByUsername(memberDTO.getUsername(),false);

        if (result.isPresent()) {
            return "fail";
        }

        Member member = dtoToEntity(memberDTO);
        memberRepository.save(member);
        return "success";
    }
}
