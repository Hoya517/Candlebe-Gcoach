package com.candlebe.gcoach.service;

import com.candlebe.gcoach.dto.MemberDTO;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.entity.MemberRole;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public interface JoinService {

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    String checkUsername(MemberDTO memberDTO);

    String join(MemberDTO memberDTO);

    // BoardDTO -> Entity 변환
    default Member dtoToEntity(MemberDTO dto) {

        Member member = Member.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .nickname(dto.getName())
                .phone(dto.getPhone())
                .formSocial(false)
                .build();

        member.addMemberRole(MemberRole.USER);
        return member;
    }

}
