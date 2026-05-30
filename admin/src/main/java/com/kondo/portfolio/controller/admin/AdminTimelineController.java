package com.kondo.portfolio.controller.admin;

import com.kondo.portfolio.admin.form.TimelineEventForm;
import com.kondo.portfolio.domain.TimelineEvent;
import com.kondo.portfolio.service.TimelineEventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Timeline (略歴) の管理画面
 */
@Controller
@RequestMapping("/admin/timeline")
public class AdminTimelineController {

    private final TimelineEventService service;

    public AdminTimelineController(TimelineEventService service) {
        this.service = service;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("events", service.findAllOrdered());
        return "admin/timeline/index";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        TimelineEventForm form = new TimelineEventForm();
        form.setPublished(true);
        form.setSortOrder(0);
        model.addAttribute("event", form);
        model.addAttribute("isNew", true);
        return "admin/timeline/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        return service.findById(id).map(ev -> {
            model.addAttribute("event", TimelineEventForm.fromEntity(ev));
            model.addAttribute("isNew", false);
            return "admin/timeline/form";
        }).orElseGet(() -> {
            redirect.addFlashAttribute("error", "見つかりませんでした");
            return "redirect:/admin/timeline";
        });
    }

    @PostMapping
    public String create(@ModelAttribute("event") TimelineEventForm form, RedirectAttributes redirect) {
        service.save(form.toEntity());
        redirect.addFlashAttribute("message", "作成しました");
        return "redirect:/admin/timeline";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("event") TimelineEventForm form, RedirectAttributes redirect) {
        Optional<TimelineEvent> updated = service.update(id, form::applyTo);
        if (updated.isEmpty()) {
            redirect.addFlashAttribute("error", "見つかりませんでした");
        } else {
            redirect.addFlashAttribute("message", "更新しました");
        }
        return "redirect:/admin/timeline";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        service.deleteById(id);
        redirect.addFlashAttribute("message", "削除しました");
        return "redirect:/admin/timeline";
    }
}
