package com.candlebe.gcoach.service;

import com.candlebe.gcoach.dto.MemberDTO;
import com.candlebe.gcoach.security.dto.AuthMemberDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface MemberService {
    String checkNickname(String nickname);
    MemberDTO authMemberDtoToMemberDto(@AuthenticationPrincipal AuthMemberDTO authMemberDTO);
}