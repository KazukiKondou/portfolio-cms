package com.kondo.portfolio.controller.admin;

import com.kondo.portfolio.domain.Skill;
import com.kondo.portfolio.service.SkillService;
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
 * スキルの管理画面（CRUD）
 */
@Controller
@RequestMapping("/admin/skills")
public class AdminSkillsController {

    private final SkillService service;

    public AdminSkillsController(SkillService service) {
        this.service = service;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("skills", service.findAllOrdered());
        return "admin/skills/index";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        Skill s = new Skill();
        s.setPublished(true);
        s.setProficiency(2);
        s.setSortOrder(0);
        model.addAttribute("skill", s);
        model.addAttribute("isNew", true);
        return "admin/skills/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        return service.findById(id).map(s -> {
            model.addAttribute("skill", s);
            model.addAttribute("isNew", false);
            return "admin/skills/form";
        }).orElseGet(() -> {
            redirect.addFlashAttribute("error", "見つかりませんでした");
            return "redirect:/admin/skills";
        });
    }

    @PostMapping
    public String create(@ModelAttribute("skill") Skill skill, RedirectAttributes redirect) {
        skill.setId(null);
        service.save(skill);
        redirect.addFlashAttribute("message", "作成しました");
        return "redirect:/admin/skills";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("skill") Skill skill, RedirectAttributes redirect) {
        Optional<Skill> updated = service.update(id, existing -> {
            existing.setName(skill.getName());
            existing.setProficiency(skill.getProficiency() == null ? 2 : skill.getProficiency());
            existing.setSortOrder(skill.getSortOrder() == null ? 0 : skill.getSortOrder());
            existing.setPublished(skill.getPublished() != null && skill.getPublished());
        });
        if (updated.isEmpty()) {
            redirect.addFlashAttribute("error", "見つかりませんでした");
        } else {
            redirect.addFlashAttribute("message", "更新しました");
        }
        return "redirect:/admin/skills";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        service.deleteById(id);
        redirect.addFlashAttribute("message", "削除しました");
        return "redirect:/admin/skills";
    }
}
