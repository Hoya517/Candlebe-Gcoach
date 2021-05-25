package com.candlebe.gcoach.admin;

import com.candlebe.gcoach.dto.ContentUploadDTO;
import com.candlebe.gcoach.entity.Content;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.*;
import com.candlebe.gcoach.service.ContentService;
import com.candlebe.gcoach.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
public class AdminController {

    private final ContentService contentService;
    private final MemberServiceImpl memberService;
    private final MemberRepository memberRepository;
    private final HistoryRepository historyRepository;
    private final LikeRepository likeRepository;
    private final ReplyRepository replyRepository;
    private final DiaryRepository diaryRepository;

    //members
    @GetMapping("/admin/members")
    public String adminMembers(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);

        return "admin_member";
    }
    // delete_member
    @PostMapping("/admin/members/{mid}/delete")
    public String deleteMember(@PathVariable("mid") Long mid) {
        Member member = memberRepository.findById(mid).get();
        historyRepository.deleteHistories(member);
        likeRepository.deleteLikes(member);
        replyRepository.deleteReplies(member);
        diaryRepository.deleteDiaries(member);
        member.getRoleSet().clear();
        memberRepository.save(member);
        memberRepository.deleteMember(member.getMid());

        return "redirect:/admin/members";
    }

    //contents
    @GetMapping("/admin/contents")
    public String adminContents(Model model) {
        List<Content> contents = contentService.findContents();
        model.addAttribute("contents", contents);

        return "admin_content";
    }

    // delete_content
    @PostMapping("/admin/contents/{cid}/delete")
    public String deleteContent(@PathVariable("cid") Long cid) {
        contentService.deleteContent(cid);

        return "redirect:/admin/contents";
    }

    //upload
    @GetMapping("/admin/contents/upload")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("contentUploadDTO", new ContentUploadDTO());

        return "admin_upload";
    }
    @PostMapping("/admin/contents/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("img") MultipartFile img,
                                   RedirectAttributes redirectAttributes,
                                   ContentUploadDTO dto) {

        contentService.save(file, img, dto);
        redirectAttributes.addFlashAttribute("message",
                "업로드 성공 :: " + file.getOriginalFilename());

        return "redirect:/admin/contents/upload";
    }
}
