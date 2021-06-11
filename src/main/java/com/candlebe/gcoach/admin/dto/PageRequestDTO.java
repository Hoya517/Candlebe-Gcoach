package com.candlebe.gcoach.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
// 화면에서 전달되는 목록 관련 데이터
// 사용 목적 : JPA 쪽에서 사용하는 Pageable 타입 객체 생성
public class PageRequestDTO {
    private int page;
    private int size;

    private String type; // 검색 조건
    private String keyword; // 검색 키워드

    public PageRequestDTO() {
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable(Sort sort) {
        // 페이지 번호가 0부터 시작한다는 점을 감안해
        // 1페이지의 경우 0이 될 수 있도록 page-1
        return PageRequest.of(page - 1, size, sort);
    }
}



