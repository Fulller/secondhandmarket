package com.secondhandmarket.controller.thymeleaf;

import com.secondhandmarket.dto.AttributeRequest;
import com.secondhandmarket.model.Attribute;
import com.secondhandmarket.service.attribute.AttributeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/attribute")

public class AttributeController {

    @Autowired
    private final AttributeService attributeService;

    @GetMapping("/add-attribute")
    public String showAddAttributeForm(Model model) {
        model.addAttribute("attributeRequest", new AttributeRequest());
        return "/attribute/add-attribute";
    }

    @PostMapping("/add-attribute-post")
    public String addAttribute(@Valid @ModelAttribute("attributeRequest") AttributeRequest attributeRequest, RedirectAttributes redirectAttributes) {
        try {
            attributeService.saveAttribute(attributeRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm thuộc tính thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/attribute/add-attribute";
    }


    @GetMapping("/list-attribute")
    public String listAttribute(Model model) {
        return "attribute/list-attribute";
    }
}
