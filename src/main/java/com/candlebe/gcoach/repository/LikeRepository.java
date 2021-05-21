package com.candlebe.gcoach.repository;

import com.candlebe.gcoach.entity.Content;
import com.candlebe.gcoach.entity.Likes;
import com.candlebe.gcoach.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {

    // 특정 콘텐츠에 특정 사용자가 좋아요를 등록한 적이 있는지 확인
    Optional<Likes> findByMemberAndContent(Member member, Content content);

    // 좋아요 조회
    @Query("SELECT l.lid FROM Likes l WHERE l.member = :member AND l.content = :content")
    Long findLikes(Member member, Content content);

    // 좋아요 개수
    @Query("SELECT COUNT(l) FROM Likes l WHERE l.content = :content")
    int likeCount(Content content);
}
