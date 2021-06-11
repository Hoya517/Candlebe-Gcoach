package com.candlebe.gcoach;

import com.candlebe.gcoach.repository.ContentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SpringBootTest
public class ContentTests {

    @Autowired
    private ContentRepository contentRepository;

    @Test
    public void testSearchPage() {
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by("cid").ascending().and(Sort.by("title").ascending())
        );
        Page<Object[]> result = contentRepository.searchPage("호흡", "테스트", pageable);
    }
}
