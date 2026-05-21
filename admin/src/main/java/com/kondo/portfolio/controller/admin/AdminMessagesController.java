package com.kondo.portfolio.controller.admin;

import com.kondo.portfolio.service.ContactMessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * お問い合わせメッセージの管理（閲覧 / 既読化 / 削除）
 */
@Controller
@RequestMapping("/admin/messages")
public class AdminMessagesController {

    private final ContactMessageService service;

    public AdminMessagesController(ContactMessageService service) {
        this.service = service;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("messages", service.findAllRecentFirst());
        return "admin/messages/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        return service.findById(id).map(m -> {
            // 表示したタイミングで既読にする
            if (m.isUnread()) {
                service.markAsRead(id);
            }
            model.addAttribute("message", m);
            return "admin/messages/show";
        }).orElseGet(() -> {
            redirect.addFlashAttribute("error", "見つかりませんでした");
            return "redirect:/admin/messages";
        });
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        service.deleteById(id);
        redirect.addFlashAttribute("message", "削除しました");
        return "redirect:/admin/messages";
    }
}
