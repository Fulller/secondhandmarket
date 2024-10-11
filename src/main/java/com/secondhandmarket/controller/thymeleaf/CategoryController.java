package com.secondhandmarket.controller.thymeleaf;

import com.secondhandmarket.dto.category.CategoryChildRequest;
import com.secondhandmarket.dto.category.CategoryParentRequest;
import com.secondhandmarket.model.Attribute;
import com.secondhandmarket.model.Category;
import com.secondhandmarket.repository.CategoryRepository;
import com.secondhandmarket.service.attribute.AttributeService;
import com.secondhandmarket.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor

public class CategoryController {

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private final AttributeService attributeService;

    @GetMapping("/add-category-parent")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCategoryForm(Model model) {
        model.addAttribute("categoryParentRequest", new CategoryParentRequest());
        return "/category/add-category-parent";
    }

    @PostMapping("/add-category-parent-post")
    @PreAuthorize("hasRole('ADMIN')")
    public String addCategoryParent(@ModelAttribute("categoryParentRequest") @Valid CategoryParentRequest categoryParentRequest,
                                    BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/category/add-category-parent";
        }
        if (categoryRepository.existsByName(categoryParentRequest.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Tên danh mục đã tồn tại!");
            return "redirect:/category/add-category-parent";
        }
        categoryService.saveCategoryParent(categoryParentRequest);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm danh mục cha thành công!");
        return "redirect:/category/add-category-parent";
    }

    @GetMapping("/add-category-child")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCategoryChildForm(Model model) {
        model.addAttribute("categoryChildRequest", new CategoryChildRequest());
        List<Category> parentCategories = categoryService.findAllCategoryParent();
        List<Attribute> attributes = attributeService.findAll();
        model.addAttribute("parentCategories", parentCategories);
        model.addAttribute("attributes", attributes);
        return "/category/add-category-child";
    }

//    @PostMapping("/add-category-child-post")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String addCategoryChild(@ModelAttribute("categoryChildRequest") @Valid CategoryChildRequest categoryChildRequest,
//                                    BindingResult bindingResult, RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors()) {
//            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
//            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
//            return "redirect:/category/add-category-child";
//        }
//        if (categoryRepository.existsByName(categoryChildRequest.getName())) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Tên danh mục đã tồn tại!");
//            return "redirect:/category/add-category-child";
//        }
//        categoryService.saveCategoryChild(categoryChildRequest);
//        redirectAttributes.addFlashAttribute("successMessage", "Thêm danh mục con thành công!");
//        return "redirect:/category/add-category-child";
//    }

    @PostMapping("/add-category-child")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addCategoryChild(@ModelAttribute("categoryChildRequest") @Valid CategoryChildRequest categoryChildRequest,
                                         BindingResult bindingResult) {
        if (categoryRepository.existsByName(categoryChildRequest.getName())) {
            bindingResult.rejectValue("name", "error.category", "Tên danh mục đã tồn tại!");
        }
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("/category/add-category-child");
            List<Category> parentCategories = categoryService.findAllCategoryParent();
            List<Attribute> attributes = attributeService.findAll();
            modelAndView.addObject("parentCategories", parentCategories);
            modelAndView.addObject("attributes", attributes);
            modelAndView.addObject("categoryParentRequest", new CategoryParentRequest());
            return modelAndView;
        }
        categoryService.saveCategoryChild(categoryChildRequest);
        modelAndView.addObject("successMessage", "Thêm danh mục con thành công!");
        List<Category> parentCategories = categoryService.findAllCategoryParent();
        List<Attribute> attributes = attributeService.findAll();
        modelAndView.addObject("parentCategories", parentCategories);
        modelAndView.addObject("attributes", attributes);
        modelAndView.addObject("categoryParentRequest", new CategoryParentRequest());
        modelAndView.setViewName("/category/add-category-child");
        return modelAndView;
    }

    @GetMapping("/list-category")
    @PreAuthorize("hasRole('ADMIN')")
    public String listCategories(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoryPage = categoryService.findAll(pageable);
        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categoryPage.getTotalPages());
        return "category/list-category";
    }
}
