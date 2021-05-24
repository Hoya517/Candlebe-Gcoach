package com.candlebe.gcoach;

import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.entity.MemberRole;
import com.candlebe.gcoach.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

@SpringBootTest
public class AdminTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Commit
    public void insert() {
        Member member = Member.builder()
                .username("admin")
                .password(passwordEncoder.encode("1111"))
                .name("관리자1")
                .nickname("관리자1")
                .phone("010-8234-0652")
                .build();
        member.addMemberRole(MemberRole.ADMIN);
        memberRepository.save(member);
    }
}