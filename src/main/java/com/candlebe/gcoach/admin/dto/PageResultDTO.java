package com.candlebe.gcoach.admin.dto;


import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
// 화면에서 필요한 결과
// 다양한 곳에서 사용할 수 있도록 제네릭 타입을 사용
public class PageResultDTO<DTO, EN> {
    private List<DTO> dtoList;      // DTO 리스트
    private int totalPage;          // 총 페이지 번호
    private int page;               // 현재 페이지 번호
    private int size;               // 목록 사이즈 (한 화면에 보여줄 컨텐츠 개수)
    private int start, end;         // 시작 페이지 번호, 끝 페이지 번호
    private boolean prev, next;     // 이전, 다음
    private List<Integer> pageList; // 페이지 목록 번호

    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {
        // Page<Entity> 를 DTO 로 변환 후 List 로 변환해 doList 에 저장
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {
        this.page = pageable.getPageNumber() + 1; // 0부터 시작하므로 1을 추가
        this.size = pageable.getPageSize();

        // temp end page
        int tempEnd = (int) (Math.ceil(page / 10.0)) * 10;
        start = tempEnd - 9;
        prev = start > 1;
        end = totalPage > tempEnd ? tempEnd : totalPage;
        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}











