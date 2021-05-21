package com.candlebe.gcoach.like;


import com.candlebe.gcoach.dto.PlayDTO;
import com.candlebe.gcoach.entity.Content;
import com.candlebe.gcoach.entity.Likes;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.ContentRepository;
import com.candlebe.gcoach.repository.LikeRepository;
import com.candlebe.gcoach.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class LikeTests {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ContentRepository contentRepository;
    @Autowired
    LikeRepository likeRepository;

    @Test
    @Commit
    void testCreateLike() throws Exception {
        mockMvc.perform(post("/like"))
                .andExpect(status().isOk());

        Likes like = likeRepository.findAll().get(0);

        assertNotNull(like);
        assertNotNull(like.getMember().getMid());
        assertNotNull(like.getContent().getCid());
    }

    @Test
    void countTest() {
        Member member = memberRepository.findById(1L).orElseThrow();
        Content content = contentRepository.findById(1L).orElseThrow();
        System.out.println(likeRepository.findLikes(member, content));
    }
}
