package com.candlebe.gcoach.admin.dto;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminMemberDTO {

    private Long mid;

    private String username; // 아이디

    private String password; // 비밀번호

    private String name; // 이름

    private String nickname; // 닉네임

    private String phone; // 전화번호

    private boolean formSocial; // 소셜 로그인 유무

    private String socialType; // 소셜 로그인 타입

    private String emotion; // 감정

    private String interest; // 관심사
}
