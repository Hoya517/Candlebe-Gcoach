package com.candlebe.gcoach.admin.service;

import com.candlebe.gcoach.admin.dto.AdminContentDTO;
import com.candlebe.gcoach.admin.dto.AdminMemberDTO;
import com.candlebe.gcoach.admin.dto.PageRequestDTO;
import com.candlebe.gcoach.admin.dto.PageResultDTO;
import com.candlebe.gcoach.entity.Content;
import com.candlebe.gcoach.entity.Member;

public interface AdminService {

    PageResultDTO<AdminMemberDTO, Object[]> getMemberList(PageRequestDTO pageRequestDTO);
    PageResultDTO<AdminContentDTO, Object[]> getContentList(PageRequestDTO pageRequestDTO);

    default AdminMemberDTO memberToAdminMemberDTO(Member member) {
        return AdminMemberDTO.builder()
                .mid(member.getMid())
                .username(member.getUsername())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .name(member.getName())
                .phone(member.getPhone())
                .formSocial(member.isFormSocial())
                .socialType(member.getSocialType())
                .interest(member.getInterest())
                .emotion(member.getEmotion())
                .build();
    }

    default AdminContentDTO contentToAdminContentDTO(Content content) {
        return AdminContentDTO.builder()
                .cid(content.getCid())
                .title(content.getTitle())
                .content(content.getContent())
                .category1(content.getCategory1())
                .category2(content.getCategory2())
                .category3(content.getCategory3())
                .build();
    }
}
