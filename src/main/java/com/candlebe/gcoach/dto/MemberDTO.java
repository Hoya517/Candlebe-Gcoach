package com.candlebe.gcoach.dto;

import lombok.*;
import javax.validation.constraints.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO{
    private Long mid;

    @NotBlank
    @Pattern(regexp = "[A-za-z0-9]{6,18}", message = "올바른 형식의 아이디를 입력해주세요.")
    private String username; // 아이디

    @NotBlank(message = "비밀번호는 필수 입력 정보입니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "올바른 형식의 비밀번호를 입력해주세요.")
    private String password; // 비밀번호

    @NotBlank(message = "비밀번호 확인은 필수 입력정보 입니다.")
    private String confirmPassword; // 비밀번호 확인

    @Builder.Default
    @AssertTrue(message = "비밀번호가 일치하지 않습니다.")
    private boolean checkPassword = false;

    @NotBlank(message = "이름은 필수 입력정보 입니다.")
    @Pattern(regexp = "[가-힣]{2,6}", message = "올바른 형식의 이름을 입력해주세요.")
    private String name; // 이름

    @NotBlank
    @Pattern(regexp = "[A-za-z0-9가-힣]{2,8}", message = "올바른 형식의 닉네임을 입력해주세요.")
    private String nickname; // 닉네임

    @NotBlank(message = "휴대폰 번호는 필수 입력정보 입니다.")
    @Pattern(regexp = "01([0|1|6|7|8|9]?)?([0-9]{3,4})?([0-9]{4})", message = "올바른 형식의 휴대폰 번호를 입력해주세요.")
    private String phone; // 전화번호

    private boolean formSocial; // 소셜 로그인 유무

    private String socialType; // 소셜 로그인 타입

    private String emotion; // 감정

    private String interest; // 관심사

    private boolean checkLogin;

    private boolean admin;
}