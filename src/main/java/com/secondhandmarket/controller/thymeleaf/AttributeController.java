package com.secondhandmarket.controller.thymeleaf;

import com.secondhandmarket.dto.attribute.AttributeRequest;
import com.secondhandmarket.model.Attribute;
import com.secondhandmarket.repository.AttributeRepository;
import com.secondhandmarket.service.attribute.AttributeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/attribute")

public class AttributeController {

    @Autowired
    private final AttributeService attributeService;
    private final AttributeRepository attributeRepository;

    @GetMapping("/add-attribute")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddAttributeForm(Model model) {
        model.addAttribute("attributeRequest", new AttributeRequest());
        return "/attribute/add-attribute";
    }

    @PostMapping("/add-attribute-post")
    @PreAuthorize("hasRole('ADMIN')")
    public String addAttribute(@ModelAttribute("attributeRequest") @Valid AttributeRequest attributeRequest,
                                     BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/attribute/add-attribute";
        }
        if (attributeRepository.existsByName(attributeRequest.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Tên thuộc tính đã tồn tại!");
            return "redirect:/attribute/add-attribute";
        }
        attributeService.saveAttribute(attributeRequest);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm thuộc tính thành công!");
        return "redirect:/attribute/add-attribute";
    }

    @GetMapping("/update-attribute/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showUpdateAttribute(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Attribute> optionalAttribute = attributeService.findById(id);
        if (optionalAttribute.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Thuộc tính không tồn tại!");
            return "redirect:/attribute/list-attribute";
        }
        Attribute attribute = optionalAttribute.get();
        AttributeRequest attributeRequest = new AttributeRequest();
        attributeRequest.setName(attribute.getName());
        model.addAttribute("attributeRequest", attributeRequest);
        model.addAttribute("id", id);
        return "attribute/update-attribute";
    }

    @PostMapping("/update-attribute-put/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateAttribute(@PathVariable("id") String id, @ModelAttribute("attributeRequest") @Valid AttributeRequest attributeRequest,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/attribute/update-attribute/" + id;
        }
        if (attributeRepository.existsByName(attributeRequest.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Tên thuộc tính đã tồn tại!");
            return "redirect:/attribute/update-attribute/" + id;
        }
        attributeService.updateAttribute(id, attributeRequest);
        redirectAttributes.addFlashAttribute("successMessage", "Sửa thuộc tính thành công!");
        return "redirect:/attribute/update-attribute/" + id;
    }

    @GetMapping("/delete-attribute/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteAttribute (@PathVariable("id") String id){
        attributeService.deleteAttribute(id);
        return "redirect:/attribute/list-attribute";
    }

    @GetMapping("/list-attribute")
    @PreAuthorize("hasRole('ADMIN')")
    public String listAttributes(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Attribute> attributePage = attributeService.findAll(pageable);
        model.addAttribute("attributes", attributePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", attributePage.getTotalPages());
        return "attribute/list-attribute";
    }
}
