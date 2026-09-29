package com.tuanhv.tripgoapi.controller.admin;

import com.tuanhv.tripgoapi.dto.admin.AdminItineraryForm;
import com.tuanhv.tripgoapi.dto.admin.AdminTourForm;
import com.tuanhv.tripgoapi.dto.admin.AdminTourFormOptions;
import com.tuanhv.tripgoapi.entity.Tour;
import com.tuanhv.tripgoapi.exception.ConflictException;
import com.tuanhv.tripgoapi.service.admin.AdminTourService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;

@Controller
@RequestMapping("/admin/tours")
@RequiredArgsConstructor
public class AdminTourController {

    private final AdminTourService adminTourService;

    @GetMapping
    public String list(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model
    ) {

        Page<Tour> result = adminTourService.search(q, page, size);

        model.addAttribute("result", result);
        model.addAttribute("q", q);
        model.addAttribute("page", page);

        return "admin/tours/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        AdminTourForm form = new AdminTourForm();
        AdminItineraryForm firstDay = new AdminItineraryForm();
        firstDay.setDayNumber(1);
        form.setItineraries(new ArrayList<>());
        form.getItineraries().add(firstDay);

        model.addAttribute("form", form);
        model.addAttribute("mode", "create");

        loadReferenceData(model);

        return "admin/tours/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("form") AdminTourForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        validatePrice(form, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "create");
            loadReferenceData(model);
            return "admin/tours/form";
        }

        try {
            adminTourService.create(form);
        } catch (ConflictException ex) {
            bindingResult.rejectValue("slug", "slug.duplicate", ex.getMessage());
            model.addAttribute("mode", "create");
            loadReferenceData(model);
            return "admin/tours/form";
        }

        redirectAttributes.addFlashAttribute(
                "success",
                "Tạo tour thành công"
        );

        return "redirect:/admin/tours";
    }

    @GetMapping("/{id}/edit")
    public String editForm(
            @PathVariable Long id,
            Model model
    ) {
        model.addAttribute("form", adminTourService.getEditForm(id));
        model.addAttribute("tourId", id);
        model.addAttribute("mode", "edit");

        loadReferenceData(model);
        return "admin/tours/form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("form") AdminTourForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        validatePrice(form, bindingResult);

        if (bindingResult.hasErrors()) {
            prepareEditModel(id, model);
            return "admin/tours/form";
        }

        try {
            adminTourService.update(id, form);
        } catch (ConflictException ex) {
            bindingResult.rejectValue(
                    "slug",
                    "slug.duplicate",
                    ex.getMessage()
            );
            prepareEditModel(id, model);
            return "admin/tours/form";
        }

        redirectAttributes.addFlashAttribute(
                "success",
                "Cập nhật tour thành công"
        );

        return "redirect:/admin/tours";
    }

    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            adminTourService.delete(id);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Xóa tour thành công"
            );
        } catch (ConflictException ex) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    ex.getMessage()
            );
        }

        return "redirect:/admin/tours";
    }

    private void validatePrice(AdminTourForm form, BindingResult bindingResult) {
        BigDecimal price = form.getPrice();
        BigDecimal discountPrice = form.getDiscountPrice();

        if (price == null) {
            return;
        }

        if (discountPrice == null) {
            return;
        }

        if (discountPrice.compareTo(price) >= 0) {
            bindingResult.rejectValue(
                    "discountPrice",
                    "discountPrice.invalid",
                    "Giá khuyến mại phải nhỏ hơn giá gốc"
            );
        }
    }

    private void prepareEditModel(Long id, Model model) {
        model.addAttribute("tourId", id);
        model.addAttribute("mode", "edit");
        loadReferenceData(model);
    }

    private void loadReferenceData(Model model) {
        AdminTourFormOptions options = adminTourService.getFormOptions();

        model.addAttribute(
                "destinations",
                options.destinations()
        );

        model.addAttribute(
                "categories",
                options.categories()
        );
    }
}
