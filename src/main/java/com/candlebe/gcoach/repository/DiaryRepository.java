package com.candlebe.gcoach.repository;


import com.candlebe.gcoach.entity.Diary;
import com.candlebe.gcoach.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    @Query("SELECT d FROM Diary d WHERE d.year = :year and d.month = :month and d.date = :date and d.member = :member")
    List<Diary> findByDateAndMember(String year, String month, String date, Member member);

    @Query("SELECT d FROM Diary d WHERE d.member = :member")
    List<Diary> findByMember(Member member);
}
