package com.candlebe.gcoach.repository;

import com.candlebe.gcoach.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ContentRepository extends JpaRepository<Content, Long> {

    // 해당 카테고리로 콘텐츠 조회
    @Query("SELECT t FROM Content t WHERE t.category1 = :category or t.category2 = :category or t.category3 = :category")
    List<Content> findByCategory(String category);

    @Query("SELECT t FROM Content t " +
            "WHERE t.category1 = :category1 or t.category2 = :category1 or t.category3 = :category1 " +
            "or t.category1 = :category2 or t.category2 = :category2 or t.category3 = :category2 ")
    List<Content> findByCategory(String category1, String category2);

    @Query("SELECT t FROM Content t " +
            "WHERE t.category1 = :category1 or t.category2 = :category1 or t.category3 = :category1 " +
            "or t.category1 = :category2 or t.category2 = :category2 or t.category3 = :category2 " +
            "or t.category1 = :category3 or t.category2 = :category3 or t.category3 = :category3")
    List<Content> findByCategory(String category1, String category2, String category3);

    // 관심 분야와 감정으로 추천 콘텐츠 조회
    @Query("SELECT t FROM Content t WHERE t.category1 = :interest or t.category1 = :emotion " +
            "                          or t.category2 = :interest or t.category2 = :emotion" +
            "                          or t.category3 = :interest or t.category3 = :emotion")
    List<Content> findByUsers(String interest, String emotion);

    @Query("SELECT t FROM Content t WHERE t.cid = :id")
    Optional<Content> findById(Long id);

    // 검색 기능
    @Query("SELECT t from Content t WHERE t.title LIKE %:search%")
    List<Content> findBySearch(String search);

    // 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM Content c WHERE c.cid = :cid")
    void deleteContent(Long cid);
}
