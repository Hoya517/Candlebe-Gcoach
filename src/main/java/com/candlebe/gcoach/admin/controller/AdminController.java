package com.candlebe.gcoach.admin.controller;

import com.candlebe.gcoach.admin.dto.PageRequestDTO;
import com.candlebe.gcoach.admin.service.AdminService;
import com.candlebe.gcoach.dto.ContentUploadDTO;
import com.candlebe.gcoach.entity.Content;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.repository.*;
import com.candlebe.gcoach.service.ContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@Log4j2
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final ContentService contentService;
    private final MemberRepository memberRepository;
    private final HistoryRepository historyRepository;
    private final LikeRepository likeRepository;
    private final ReplyRepository replyRepository;
    private final DiaryRepository diaryRepository;
    private final AdminService adminService;

    //members
    @GetMapping("/admin/members")
    public String adminMembers(PageRequestDTO pageRequestDTO, Model model) {
        model.addAttribute("result", adminService.getMemberList(pageRequestDTO));
        return "admin_member";
    }
    // delete_member
    @PostMapping("/admin/members/{mid}/delete")
    public String deleteMember(@PathVariable("mid") Long mid, String url) {
        Member member = memberRepository.findById(mid).get();
        historyRepository.deleteHistories(member);
        likeRepository.deleteLikes(member);
        replyRepository.deleteReplies(member);
        diaryRepository.deleteDiaries(member);
        member.getRoleSet().clear();
        memberRepository.save(member);
        memberRepository.deleteMember(member.getMid());
        return "redirect:" + url;
    }

    // contents
    @GetMapping("/admin/contents")
    public String searchContents(PageRequestDTO pageRequestDTO, Model model) {
        model.addAttribute("result", adminService.getContentList(pageRequestDTO));
        return "admin_content";
    }

    // delete_content
    @PostMapping("/admin/contents/{cid}/delete")
    public String deleteContent( @PathVariable("cid") Long cid, String url) {
        Content content = contentService.findOne(cid).get();
        historyRepository.deleteHistories(content);
        likeRepository.deleteLikes(content);
        replyRepository.deleteReplies(content);
        contentService.deleteContent(cid);
        return "redirect:" + url;
    }

    //content
    @GetMapping("/admin/contents/{cid}")
    public String selectContent(@PathVariable("cid") Long cid, Model model) {
        Content content = contentService.findOne(cid).get();

        model.addAttribute("content", content);

        return "admin_content_cid";
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
        redirectAttributes.addFlashAttribute("message", file.getOriginalFilename());

        return "redirect:/admin/contents/upload";
    }
}
