package com.secondhandmarket.controller.thymeleaf;

import com.secondhandmarket.dto.attribute.AttributeRequest;
import com.secondhandmarket.model.Attribute;
import com.secondhandmarket.model.Category;
import com.secondhandmarket.repository.AttributeRepository;
import com.secondhandmarket.service.attribute.AttributeService;
import com.secondhandmarket.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/attribute")

public class AttributeController {

    private final AttributeService attributeService;
    private final AttributeRepository attributeRepository;
    private final CategoryService categoryService;

    @GetMapping("/add-attribute")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView showAddAttributeForm( ) {
        ModelAndView modelAndView = new ModelAndView("attribute/add-attribute");
        List<Category> categoryList = categoryService.findAllCategoryChild();
        modelAndView.addObject("categories", categoryList);
        modelAndView.addObject("attributeRequest", new AttributeRequest());
        return modelAndView;
    }

    @PostMapping("/add-attribute")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addAttribute(@ModelAttribute("attributeRequest") @Valid AttributeRequest attributeRequest,
                                     BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("fieldErrors", bindingResult.getFieldErrors());
            List<Category> categoryList = categoryService.findAllCategoryChild();
            modelAndView.addObject("categories", categoryList);
            modelAndView.addObject("attributeRequest", attributeRequest);  // Giữ lại giá trị đã nhập
            modelAndView.setViewName("attribute/add-attribute");
            return modelAndView;
        }
        attributeService.saveAttribute(attributeRequest);
        modelAndView.addObject("successMessage", "Thêm thuộc tính thành công!");
        List<Category> categoryList = categoryService.findAllCategoryChild();
        modelAndView.addObject("categories", categoryList);
        modelAndView.addObject("attributeRequest", new AttributeRequest());  // Tạo form mới
        modelAndView.setViewName("attribute/add-attribute");
        return modelAndView;
    }


    @GetMapping("/update-attribute/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView showUpdateAttribute(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        Optional<Attribute> optionalAttribute = attributeService.findById(id);
        ModelAndView modelAndView = new ModelAndView();
        if (optionalAttribute.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Thuộc tính không tồn tại!");
            modelAndView.setViewName("redirect:/attribute/list-attribute");
            return modelAndView;
        }
        Attribute attribute = optionalAttribute.get();
        AttributeRequest attributeRequest = new AttributeRequest();
        attributeRequest.setName(attribute.getName());
        attributeRequest.setIsEnter(attribute.getIsEnter());
        attributeRequest.setIsRequired(attribute.getIsRequired());
        modelAndView.addObject("attributeRequest", attributeRequest);
        modelAndView.addObject("id", id);
        modelAndView.setViewName("attribute/update-attribute");
        return modelAndView;
    }

    @PostMapping("/update-attribute/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateAttribute(@PathVariable("id") String id, @ModelAttribute("attributeRequest") @Valid AttributeRequest attributeRequest,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/attribute/update-attribute/" + id;
        }
        attributeService.updateAttribute(id, attributeRequest);
        redirectAttributes.addFlashAttribute("successMessage", "Sửa thuộc tính thành công!");
        return "redirect:/attribute/list-attribute";
    }

    @GetMapping("/delete-attribute/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView deleteAttribute (@PathVariable("id") String id, RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView();
        attributeService.deleteAttribute(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa thuộc tính thành công!");
        modelAndView.setViewName("redirect:/attribute/list-attribute");
        return modelAndView;
    }

    @GetMapping("/list-attribute")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView listAttributes(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        ModelAndView modelAndView = new ModelAndView();
        Page<Attribute> attributePage = attributeService.findAll(pageable);
        List<Category> childCategories = categoryService.findAllCategoryChild();
        modelAndView.addObject("childCategories", childCategories);
        modelAndView.addObject("attributes", attributePage.getContent());
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", attributePage.getTotalPages());
        modelAndView.setViewName("attribute/list-attribute");
        return modelAndView;
    }
}
