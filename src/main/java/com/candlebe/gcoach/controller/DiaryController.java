package com.candlebe.gcoach.controller;

import com.candlebe.gcoach.dto.EmotionDTO;
import com.candlebe.gcoach.dto.MemberDTO;
import com.candlebe.gcoach.dto.TodaysReviewDTO;
import com.candlebe.gcoach.entity.Diary;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.DiaryRepository;
import com.candlebe.gcoach.repository.MemberRepository;
import com.candlebe.gcoach.security.dto.AuthMemberDTO;
import com.candlebe.gcoach.service.DiaryService;
import com.candlebe.gcoach.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class DiaryController {

    private final DiaryService diaryService;
    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    // 달력
    @GetMapping("/diary/calendar")
    public String calendar(Model model, @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        log.info("diary_calendar....");
        MemberDTO memberDTO = memberService.authMemberDtoToMemberDto(authMemberDTO);
        model.addAttribute("memberDTO", memberDTO);

        return "diary_calendar";
    }

    // diary_today
    @GetMapping("/diary/today/{year}/{month}/{date}")
    public String today(Model model,
                        @PathVariable("year") String year,
                        @PathVariable("month") String month,
                        @PathVariable("date") String date,
                        @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        log.info("diary_today....");
        MemberDTO memberDTO = memberService.authMemberDtoToMemberDto(authMemberDTO);
        model.addAttribute("memberDTO", memberDTO);
        Member member = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial()).orElseThrow();
        try {
            Diary diary = diaryRepository.findByDateAndMember(year, month, date, member).get(0);
            model.addAttribute("emotion", diary.getEmotion());
            model.addAttribute("todaysReview", diary.getTodaysReview());
        } catch (Exception e) {
            System.out.println("Diary not found");
        } finally {
            return "diary_today";
        }
    }

    // save today's review
    @PostMapping("/diary/today/{year}/{month}/{date}")
    public String saveTodaysReview(TodaysReviewDTO todaysReviewDTO,
                                   @PathVariable("year") String year,
                                   @PathVariable("month") String month,
                                   @PathVariable("date") String date,
                                   @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        log.info("[save] diary_today_review....");
        Member member = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial()).orElseThrow();

        boolean isDiaryEmpty = diaryService.isDiaryEmpty(year, month, date, member);
        if (isDiaryEmpty) {
            diaryService.saveTodaysReview(todaysReviewDTO.getTodaysReview(), member, year, month, date);
        } else {
            diaryService.setTodaysReview(todaysReviewDTO.getTodaysReview(), member, year, month, date);
        }

        return "redirect:/diary/today/{year}/{month}/{date}";
    }

    // diary_today_emotion
    @GetMapping("/diary/today/{year}/{month}/{date}/emotion")
    public String todayEmotion(Model model,
                                     @PathVariable("year") String year,
                                     @PathVariable("month") String month,
                                     @PathVariable("date") String date) {
        log.info("diary_today_emotion....");

        model.addAttribute("emotionDTO", new EmotionDTO());
        return "diary_today_emotion";
    }
    // save today_emotion
    @PostMapping("/diary/today/{year}/{month}/{date}/emotion")
    public String saveTodayEmotion(EmotionDTO emotionDTO,
                                 @PathVariable("year") String year,
                                 @PathVariable("month") String month,
                                 @PathVariable("date") String date,
                                 @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        log.info("[save] diary_today_emotion....");

        Member member = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial()).orElseThrow();

        boolean isDiaryEmpty = diaryService.isDiaryEmpty(year, month, date, member);
        if (isDiaryEmpty) {
            diaryService.saveTodayEmotion(emotionDTO.getEmotion(), member, year, month, date);
        } else {
            diaryService.setTodayEmotion(emotionDTO.getEmotion(), member, year, month, date);
        }
        return "redirect:/diary/today/{year}/{month}/{date}";
    }
}
