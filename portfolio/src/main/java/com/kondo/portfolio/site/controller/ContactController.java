package com.kondo.portfolio.site.controller;

import com.kondo.portfolio.site.form.ContactForm;
import com.kondo.portfolio.site.service.ContactMessageService;
import com.kondo.portfolio.site.service.PageViewService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** 公開サイトのお問い合わせフォーム */
@Controller
@RequestMapping(ContactController.PAGE_PATH)
public class ContactController {

    static final String PAGE_PATH = "/contact";

    private final ContactMessageService service;
    private final PageViewService pageViewService;

    public ContactController(ContactMessageService service, PageViewService pageViewService) {
        this.service = service;
        this.pageViewService = pageViewService;
    }

    @GetMapping
    public String form(Model model) {
        // 数えるのは表示のときだけ。送信(POST)後のリダイレクト先GETは1回として数える
        pageViewService.recordView(PAGE_PATH);
        if (!model.containsAttribute("contactForm")) {
            model.addAttribute("contactForm", new ContactForm());
        }
        return "contact";
    }

    @PostMapping
    public String submit(
            @Valid @ModelAttribute("contactForm") ContactForm form,
            BindingResult result,
            RedirectAttributes redirect) {
        if (result.hasErrors()) {
            // バリデーションエラー時は入力を保持してフォームに戻す
            return "contact";
        }
        service.receive(form.getName(), form.getEmail(), form.getMessage());
        redirect.addFlashAttribute("submitted", true);
        return "redirect:/contact";
    }
}
