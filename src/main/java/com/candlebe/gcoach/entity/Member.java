package com.candlebe.gcoach.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"roleSet", "diaryList"})
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>(); // 권한

    @OneToMany(mappedBy = "member")
    private List<Diary> diaryList = new ArrayList<>();

    public void addMemberRole(MemberRole memberRole) {
        roleSet.add(memberRole);
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

}
