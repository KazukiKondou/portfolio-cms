package com.kondo.portfolio.controller.admin;

import com.kondo.portfolio.admin.form.SettingsForm;
import com.kondo.portfolio.domain.SiteSetting;
import com.kondo.portfolio.service.SiteSettingService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** サイト設定（文言）の編集画面 */
@Controller
@RequestMapping("/admin/settings")
public class AdminSettingsController {

    private final SiteSettingService siteSettingService;

    public AdminSettingsController(SiteSettingService siteSettingService) {
        this.siteSettingService = siteSettingService;
    }

    @GetMapping
    public String edit(Model model) {
        List<SiteSetting> all = siteSettingService.findAllOrdered();
        SettingsForm form = new SettingsForm();
        for (SiteSetting s : all) {
            SettingsForm.Entry e = new SettingsForm.Entry();
            e.setKey(s.getKey());
            e.setValue(s.getValue());
            e.setDescription(s.getDescription());
            form.getEntries().add(e);
        }
        model.addAttribute("form", form);
        return "admin/settings/edit";
    }

    @PostMapping
    public String save(@ModelAttribute("form") SettingsForm form, RedirectAttributes redirect) {
        siteSettingService.updateAll(form.toUpdates());
        redirect.addFlashAttribute("message", "保存しました");
        return "redirect:/admin/settings";
    }
}
