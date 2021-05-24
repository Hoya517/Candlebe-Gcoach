package com.candlebe.gcoach.admin;

import com.candlebe.gcoach.dto.ContentUploadDTO;
import com.candlebe.gcoach.service.ContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@Log4j2
@RequiredArgsConstructor
public class AdminController {
    private final ContentService contentService;

    //Upload
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
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/admin/contents/upload";
    }
}
