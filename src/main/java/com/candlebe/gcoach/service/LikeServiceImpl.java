package com.candlebe.gcoach.service;

import com.candlebe.gcoach.entity.Content;
import com.candlebe.gcoach.entity.Likes;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.ContentRepository;
import com.candlebe.gcoach.repository.LikeRepository;
import com.candlebe.gcoach.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class LikeServiceImpl implements LikeService {

    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    private final ContentRepository contentRepository;

    public boolean addLike(Long mid, Long cid) {
        Member member = memberRepository.findById(mid).orElseThrow();
        Content content = contentRepository.findById(cid).orElseThrow();
        int likeCount = 0;
        Likes likes = Likes.builder()
                .member(member)
                .content(content)
                .build();

        // 좋아요를 누른적이 없다면 좋아요 추가
        if(isNotAlreadyLike(member, content)) {
            likeRepository.save(likes);
            likeCount = likeRepository.likeCount(content);
            content.setLikeCount(likeCount);
            content.addLikes(likes);
            contentRepository.save(content);
            return true;

            // 좋아요를 누른적이 있따면 좋아요 취소
        } else {
            Long lid = likeRepository.findLikes(member, content);
            likeRepository.deleteById(lid);
            likeCount = likeRepository.likeCount(content);
            content.setLikeCount(likeCount);
            content.deleteLikes(likes);
            contentRepository.save(content);
            return false;
        }
    }

    //사용자가 이미 좋아요 한 게시물인지 체크
    private boolean isNotAlreadyLike(Member member, Content content) {
        return likeRepository.findByMemberAndContent(member, content).isEmpty();
    }
}
