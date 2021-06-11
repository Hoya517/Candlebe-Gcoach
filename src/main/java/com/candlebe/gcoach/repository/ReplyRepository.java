package com.candlebe.gcoach.repository;

import com.candlebe.gcoach.entity.Content;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.entity.Reply;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Reply> findByContentOrderByRidDesc(Content content);

    @Modifying
    @Transactional
    @Query("DELETE FROM Reply r WHERE r.member = :member")
    void deleteReplies(Member member);

    @Modifying
    @Transactional
    @Query("DELETE FROM Reply r WHERE r.content = :content")
    void deleteReplies(Content content);
}
