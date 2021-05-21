package com.candlebe.gcoach.security;

import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.entity.MemberRole;
import com.candlebe.gcoach.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;


@SpringBootTest
public class MemberTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void insertMembers() {
        Member member = Member.builder()
                .username("admin")
                .password(passwordEncoder.encode("1111"))
                .name("관리자")
                .nickname("ADMIN")
                .phone("01082340652")
                .formSocial(false)
                .build();
        member.addMemberRole(MemberRole.ADMIN);
        memberRepository.save(member);
    }

//    @Test
//    @Transactional
//    @Commit
//    public void updateNickname() {
//        Member member = memberRepository.findByUsername("jinyedo1").orElseThrow();
//        System.out.println("변경 전 회원 정보 : " + member);
//        memberRepository.updateNickname("닉네임변경", "jinyedo1");
//        Member member2 = memberRepository.findByUsername("jinyedo1").orElseThrow();
//        System.out.println("변경 후 회원 정보 : " + member2);
//    }

    @Test
    public void passwordEquals() {
        Member member = memberRepository.findByUsername("jinyedo", false).orElseThrow();
        System.out.println(member.getPassword());
        String password = "ydakstp123@"; // true
        // String password = passwordEncoder.encode("ydakstp123@"); // false

        if (passwordEncoder.matches(password, member.getPassword())) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }
}
