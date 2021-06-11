package com.candlebe.gcoach.admin.service;

import com.candlebe.gcoach.admin.dto.AdminContentDTO;
import com.candlebe.gcoach.admin.dto.AdminMemberDTO;
import com.candlebe.gcoach.admin.dto.PageRequestDTO;
import com.candlebe.gcoach.admin.dto.PageResultDTO;
import com.candlebe.gcoach.repository.ContentRepository;
import com.candlebe.gcoach.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;

    @Override
    public PageResultDTO<AdminMemberDTO, Object[]> getMemberList(PageRequestDTO pageRequestDTO) {
        log.info(pageRequestDTO);

        // Entity -> BoardDTO 변환
        Function<Object[], AdminMemberDTO> fn = (en -> memberToAdminMemberDTO(memberRepository.findById((Long) en[0]).get()));

        Page<Object[]> result = memberRepository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("mid").ascending())
        );

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public PageResultDTO<AdminContentDTO, Object[]> getContentList(PageRequestDTO pageRequestDTO) {
        log.info(pageRequestDTO);

        // Entity -> BoardDTO 변환
        Function<Object[], AdminContentDTO> fn = (en -> contentToAdminContentDTO(contentRepository.findById((Long) en[0]).get()));

        Page<Object[]> result = contentRepository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("cid").ascending())
        );

        return new PageResultDTO<>(result, fn);
    }
}
