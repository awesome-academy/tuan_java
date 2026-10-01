package com.tuanhv.tripgoapi.controller.admin;

import com.tuanhv.tripgoapi.dto.admin.AdminDestinationForm;
import com.tuanhv.tripgoapi.dto.admin.AdminDestinationListItem;
import com.tuanhv.tripgoapi.exception.ConflictException;
import com.tuanhv.tripgoapi.service.admin.AdminDestinationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/destinations")
@RequiredArgsConstructor
public class AdminDestinationController {

    private final AdminDestinationService adminDestinationService;

    @GetMapping
    public String list(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Page<AdminDestinationListItem> result = adminDestinationService.search(
                q,
                page,
                size
        );

        model.addAttribute("result", result);
        model.addAttribute("q", q);

        return "admin/destinations/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute(
                "form",
                new AdminDestinationForm()
        );

        model.addAttribute(
                "editMode",
                false
        );

        return "admin/destinations/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("form") AdminDestinationForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(
                    "editMode",
                    false
            );

            return "admin/destinations/form";
        }

        try {
            adminDestinationService.create(form);
        } catch (ConflictException ex) {
            bindingResult.rejectValue(
                    "slug",
                    "slug.duplicate",
                    ex.getMessage()
            );

            model.addAttribute(
                    "editMode",
                    false
            );

            return "admin/destinations/form";
        }

        redirectAttributes.addFlashAttribute(
                "success",
                "Thêm điểm đến thành công"
        );

        return "redirect:/admin/destinations";
    }

    @GetMapping("/{id}/edit")
    public String editForm(
            @PathVariable Long id,
            Model model
    ) {
        AdminDestinationForm form = adminDestinationService.getEditForm(id);

        model.addAttribute("form", form);
        model.addAttribute("destinationId", id);
        model.addAttribute("editMode", true);

        return "admin/destinations/form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("form") AdminDestinationForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            prepareEditModel(model, id);
            return "admin/destinations/form";
        }

        try {
            adminDestinationService.update(id, form);
        } catch (ConflictException ex) {
            bindingResult.rejectValue(
                    "slug",
                    "slug.duplicate",
                    ex.getMessage()
            );
            prepareEditModel(model, id);

            return "admin/destinations/form";
        }

        redirectAttributes.addFlashAttribute(
                "success",
                "Cập nhật điểm đến thành công"
        );

        return "redirect:/admin/destinations";
    }

    private void prepareEditModel(Model model, Long id) {
        model.addAttribute("editMode", true);
        model.addAttribute("destinationId", id);
    }

    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            adminDestinationService.delete(id);
            redirectAttributes.addFlashAttribute(
                    "success",
                    "Xóa điểm đến thành công"
            );
        } catch (ConflictException ex) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    ex.getMessage()
            );
        }

        return "redirect:/admin/destinations";
    }
}
