package com.candlebe.gcoach.repository;

import com.candlebe.gcoach.entity.Content;
import com.candlebe.gcoach.entity.History;
import com.candlebe.gcoach.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

    @Query("SELECT MAX(h) FROM History h " +
            "WHERE h.member = :member " +
            "GROUP BY h.content " +
            "ORDER BY MAX(h) DESC")
    List<History> getHistoryWithAll(Member member);

    @Modifying
    @Transactional
    @Query("DELETE FROM History h WHERE h.member = :member")
    void deleteHistories(Member member);

    @Modifying
    @Transactional
    @Query("DELETE FROM History h WHERE h.content = :content")
    void deleteHistories(Content content);
}
