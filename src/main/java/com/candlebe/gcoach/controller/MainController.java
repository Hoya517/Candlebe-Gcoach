package com.candlebe.gcoach.controller;

import com.candlebe.gcoach.dto.InterestDTO;
import com.candlebe.gcoach.dto.MemberDTO;
import com.candlebe.gcoach.dto.PlayDTO;
import com.candlebe.gcoach.entity.Content;
import com.candlebe.gcoach.entity.History;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.ContentRepository;
import com.candlebe.gcoach.repository.HistoryRepository;
import com.candlebe.gcoach.repository.LikeRepository;
import com.candlebe.gcoach.repository.MemberRepository;
import com.candlebe.gcoach.security.dto.AuthMemberDTO;
import com.candlebe.gcoach.service.ContentService;
import com.candlebe.gcoach.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MainController {

    private final ContentService contentService;
    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    private final LikeRepository likeRepository;
    private final HistoryRepository historyRepository;
    private final MemberService memberService;

    //메인(호흡훈련)으로 이동
    @GetMapping("/")
    public String mainPage(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, Model model) {
        log.info("main Page..........");
        MemberDTO memberDTO = memberService.authMemberDtoToMemberDto(authMemberDTO);
        model.addAttribute("memberDTO", memberDTO);
        // 호흡 관련 콘텐츠 리스트 가져오기
        List<Content> contents = contentService.findContentsByCategory("호흡");
        // 가져온 콘텐츠 리스트 model 에 할당
        model.addAttribute("contents", contents);
        // 메인 페이지로 리턴
        return "main";
    }

    //메인(명상)으로 이동 - 호흡 훈련과 같은 서비스 구조로 동작
    @GetMapping("/meditation")
    public String meditationPage(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, Model model) {
        log.info("meditation Page..........");
        MemberDTO memberDTO = memberService.authMemberDtoToMemberDto(authMemberDTO);
        model.addAttribute("memberDTO", memberDTO);
        List<Content> contents = contentService.findContentsByCategory("명상");
        model.addAttribute("contents", contents);
        return "main_meditation";
    }

    //메인(추천콘텐츠)로 이동
    @GetMapping("/recommend")
    public String recommendPage(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, Model model) {
        log.info("recommend Page..........");
        MemberDTO memberDTO = memberService.authMemberDtoToMemberDto(authMemberDTO);
        model.addAttribute("memberDTO", memberDTO);
        // 조회한 회원의 감정과 관심 분야를 통해 추촌 콘텐츠 리스트 가져오기
        List<Content> contents = contentService.findContentsForUser(memberDTO.getInterest(), memberDTO.getEmotion());
        model.addAttribute("contents", contents);
        return "main_recommend";
    }

    //메인(카테고리)로 이동
    @GetMapping("/category")
    public String categoryPage(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, Model model) {
        log.info("category Page..........");
        MemberDTO memberDTO = memberService.authMemberDtoToMemberDto(authMemberDTO);
        model.addAttribute("memberDTO", memberDTO);
        model.addAttribute("interestDTO", new InterestDTO());
        return "main_category";
    }

    // 메인(카테고리)에서 카테고리 클릭 시 해당 카테고리에 해당하는 콘텐츠 조회 후 model 에 할당
    @GetMapping("/category/interest")
    public String categoryInterest(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, Model model, InterestDTO interestDTO) {
        MemberDTO memberDTO = memberService.authMemberDtoToMemberDto(authMemberDTO);
        model.addAttribute("memberDTO", memberDTO);
        List<Content> contents = contentService.findContentsByCategory(interestDTO.getInterest());
        model.addAttribute("contents", contents);
        model.addAttribute("interestName", interestDTO.getInterest());
        return "category_interest";
    }

    // 검색
    @GetMapping("/search")
    public String search(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, Model model, String search) {
        log.info("검색어 : " + search);
        MemberDTO memberDTO = memberService.authMemberDtoToMemberDto(authMemberDTO);
        model.addAttribute("memberDTO", memberDTO);
        List<Content> contents = contentService.findBySearch(search);
        System.out.println(search);
        model.addAttribute("contents", contents);
        return "search";
    }

    // 플레이 화면으로 이동
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/play")
    public String getPlay(@AuthenticationPrincipal AuthMemberDTO authMemberDTO,
                          @PathVariable("id") Long id, Model model) throws JsonProcessingException {

        log.info("---------------플레이화면---------------");
        // 로그인한 회원 정보
        Member member = memberRepository.findByUsername(authMemberDTO.getUsername(), authMemberDTO.isFormSocial()).orElseThrow();
        // id 값에 맞는 콘텐츠
        Content content = contentRepository.findById(id).orElseThrow();
        // 해당 회원이 해당 콘텐츠에 좋아요를 눌렀는지
        boolean likeCheck = likeRepository.findByMemberAndContent(member, content).isPresent();
        // 해당 콘텐츠의 총 좋아요 수
        int likeCount = likeRepository.likeCount(content);

        // 콘텐츠를 담는 리스트
        List<Content> contents = new ArrayList<>();
        // 카테고리를 담는 리스트
        ArrayList<String> categoryList = new ArrayList<>();

        // 콘텐츠의 카테고리가 null 일 경우 빈 문자열로 변환 (NullPoint Exception 방지)
        String category1 = content.getCategory1() == null ? "" : content.getCategory1();
        String category2 = content.getCategory2() == null ? "" : content.getCategory2();
        String category3 = content.getCategory3() == null ? "" : content.getCategory3();
        log.info("category1 : " + category1);
        log.info("category2 : " + category2);
        log.info("category3 : " + category3);

        // 카테고리가 빈 값이 아닐경우 카테고리리스트에 추가
        if (category1 != null && !category1.equals("")) categoryList.add(category1);
        if (category2 != null && !category2.equals("")) categoryList.add(category2);
        if (category3 != null && !category3.equals("")) categoryList.add(category3);
        log.info("categoryList : " + categoryList);

        // 카테고리에 맞는 콘텐츠들을 List 에 저장
        if (categoryList.size() == 1) { // 카테고리가 하나일 경우
            contents = contentService.findContentsByCategory(categoryList.get(0));
        } else if (categoryList.size() == 2) { // 카테고리가 두개일 경우
            contents = contentService.findContentsByCategory(categoryList.get(0), categoryList.get(1));
        } else if (categoryList.size() == 3) { // 카테고리가 세개일 경우
            contents = contentService.findContentsByCategory(categoryList.get(0), categoryList.get(1), categoryList.get(2));
        }

        // 현재 플레이중인 콘텐츠는 콘텐츠 리스트에서 제외
        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).getCid().equals(id)) {
                contents.remove(contents.get(i));
            }
        }

        PlayDTO playDTO = PlayDTO.builder()
                .mid(member.getMid())
                .nickname(member.getNickname())
                .cid(content.getCid())
                .contentName(content.getOriginalName())
                .imgOriginalName(content.getImgOriginalName())
                .likeCount(likeCount)
                .likeCheck(likeCheck)
                .build();

        History history = History.builder()
                .member(member)
                .content(content)
                .build();
        historyRepository.save(history);

        log.info("playDTO : " + playDTO);
        log.info("contents : " + contents);
        MemberDTO memberDTO = memberService.authMemberDtoToMemberDto(authMemberDTO);
        model.addAttribute("memberDTO", memberDTO);
        model.addAttribute("dto", playDTO);
        model.addAttribute("contents", contents);
        return "play";
    }
}