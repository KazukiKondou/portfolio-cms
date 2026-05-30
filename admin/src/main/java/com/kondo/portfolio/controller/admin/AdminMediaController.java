package com.kondo.portfolio.controller.admin;

import com.kondo.portfolio.service.MediaFileService;
import com.kondo.portfolio.service.SiteSettingService;
import java.io.IOException;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** 写真（ヒーロー画像）のアップロード画面 */
@Controller
@RequestMapping("/admin/media")
public class AdminMediaController {

    private final MediaFileService mediaService;
    private final SiteSettingService settingService;

    public AdminMediaController(MediaFileService mediaService, SiteSettingService settingService) {
        this.mediaService = mediaService;
        this.settingService = settingService;
    }

    @GetMapping
    public String form(@ModelAttribute("settings") Map<String, String> settings, Model model) {
        model.addAttribute("currentPhotoUrl", settings.getOrDefault("hero.photoUrl", ""));
        return "admin/media/edit";
    }

    @PostMapping
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirect)
            throws IOException {
        if (file.isEmpty()) {
            redirect.addFlashAttribute("error", "ファイルを選択してください");
            return "redirect:/admin/media";
        }
        String ct = file.getContentType();
        if (ct == null || !ct.startsWith("image/")) {
            redirect.addFlashAttribute("error", "画像ファイルを指定してください");
            return "redirect:/admin/media";
        }
        Long id = mediaService.save(file.getOriginalFilename(), ct, file.getBytes());
        // hero.photoUrl を新しい URL に差し替える
        settingService.updateAll(Map.of("hero.photoUrl", "/media/" + id));
        redirect.addFlashAttribute("message", "アップロードしました");
        return "redirect:/admin/media";
    }
}
