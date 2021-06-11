package com.candlebe.gcoach.security.service;

import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.MemberRepository;
import com.candlebe.gcoach.security.dto.AuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("MemberUserDetailsService loadUserByUsername : " + username);

        Optional<Member> result = memberRepository.findByUsername(username, false);
        if (result.isPresent()) {
            Member member = result.get();
            log.info("Member : " + member);

            AuthMemberDTO authMemberDTO = new AuthMemberDTO(
                    member.getUsername(),
                    member.getPassword(),
                    member.isFormSocial(),
                    member.getRoleSet().stream().map(role ->
                            new SimpleGrantedAuthority("ROLE_" + role.name())
                    ).collect(Collectors.toSet())
            );
            authMemberDTO.setName(member.getName());

            return authMemberDTO;
        }
        throw new UsernameNotFoundException("Check Email or Social");
    }
}
