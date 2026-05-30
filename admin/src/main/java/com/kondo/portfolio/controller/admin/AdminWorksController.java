package com.kondo.portfolio.controller.admin;

import com.kondo.portfolio.admin.form.WorkForm;
import com.kondo.portfolio.domain.Work;
import com.kondo.portfolio.service.WorkService;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** 作品一覧の管理画面（CRUD） */
@Controller
@RequestMapping("/admin/works")
public class AdminWorksController {

    private final WorkService service;

    public AdminWorksController(WorkService service) {
        this.service = service;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("works", service.findAllOrdered());
        return "admin/works/index";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        WorkForm form = new WorkForm();
        form.setPublished(true);
        form.setSortOrder(0);
        model.addAttribute("work", form);
        model.addAttribute("isNew", true);
        return "admin/works/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        return service.findById(id)
                .map(
                        w -> {
                            model.addAttribute("work", WorkForm.fromEntity(w));
                            model.addAttribute("isNew", false);
                            return "admin/works/form";
                        })
                .orElseGet(
                        () -> {
                            redirect.addFlashAttribute("error", "見つかりませんでした");
                            return "redirect:/admin/works";
                        });
    }

    @PostMapping
    public String create(@ModelAttribute("work") WorkForm form, RedirectAttributes redirect) {
        service.save(form.toEntity());
        redirect.addFlashAttribute("message", "作成しました");
        return "redirect:/admin/works";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute("work") WorkForm form,
            RedirectAttributes redirect) {
        Optional<Work> updated = service.update(id, form::applyTo);
        if (updated.isEmpty()) {
            redirect.addFlashAttribute("error", "見つかりませんでした");
        } else {
            redirect.addFlashAttribute("message", "更新しました");
        }
        return "redirect:/admin/works";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        service.deleteById(id);
        redirect.addFlashAttribute("message", "削除しました");
        return "redirect:/admin/works";
    }
}
