package com.candlebe.gcoach.service;

import com.candlebe.gcoach.entity.Diary;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;

    public boolean isDiaryEmpty(String year, String month, String date, Member member) {
        try {
            diaryRepository.findByDateAndMember(year, month, date, member).get(0);
            return false;
        } catch (Exception e) {
            System.out.println("not found");
        }
        return true;
    }

    // 오늘의 감정 (new Diary)
    public Long saveTodayEmotion(String emotion, Member member, String year, String month, String date) {
        Diary diary = Diary.builder()
                .emotion(emotion)
                .member(member)
                .year(year)
                .month(month)
                .date(date)
                .build();
        diaryRepository.save(diary);
        return diary.getId();
    }

    // 오늘의 감정 (set)
    @Transactional
    public Long setTodayEmotion(String emotion, Member member, String year, String month, String date) {
        Diary diary = diaryRepository.findByDateAndMember(year, month, date, member).get(0);
        diary.setEmotion(emotion);

        return diary.getId();
    }

    // 오늘의 후기 (new Diary)
    public Long saveTodaysReview(String todaysReview, Member member, String year, String month, String date) {
        Diary diary = Diary.builder()
                .todaysReview(todaysReview)
                .member(member)
                .year(year)
                .month(month)
                .date(date)
                .build();
        diaryRepository.save(diary);
        return diary.getId();
    }

    // 오늘의 후기 (set)
    @Transactional
    public Long setTodaysReview(String todaysReview, Member member, String year, String month, String date) {
        Diary diary = diaryRepository.findByDateAndMember(year, month, date, member).get(0);
        diary.setTodaysReview(todaysReview);

        return diary.getId();
    }

}
