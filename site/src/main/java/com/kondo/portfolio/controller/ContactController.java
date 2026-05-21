package com.kondo.portfolio.controller;

import com.kondo.portfolio.service.ContactMessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 公開サイトのお問い合わせフォーム
 */
@Controller
@RequestMapping("/contact")
public class ContactController {

    private final ContactMessageService service;

    public ContactController(ContactMessageService service) {
        this.service = service;
    }

    @GetMapping
    public String form(Model model) {
        if (!model.containsAttribute("contactForm")) {
            model.addAttribute("contactForm", new ContactForm());
        }
        return "contact";
    }

    @PostMapping
    public String submit(@Valid @ModelAttribute("contactForm") ContactForm form,
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

    public static class ContactForm {
        @NotBlank(message = "お名前は必須です")
        @Size(max = 100)
        private String name;

        @NotBlank(message = "メールアドレスは必須です")
        @Email(message = "メールアドレスの形式が不正です")
        @Size(max = 255)
        private String email;

        @NotBlank(message = "メッセージは必須です")
        @Size(max = 5000)
        private String message;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
