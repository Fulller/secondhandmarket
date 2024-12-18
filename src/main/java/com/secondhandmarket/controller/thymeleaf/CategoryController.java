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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    public ModelAndView showCategoryChildForm() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("categoryChildRequest", new CategoryChildRequest());
        List<Category> parentCategories = categoryService.findAllCategoryParent();
        List<Attribute> attributes = attributeService.findAll();
        modelAndView.addObject("parentCategories", parentCategories);
        modelAndView.addObject("attributes", attributes);
        modelAndView.setViewName("/category/add-category-child");
        return modelAndView;
    }

    @PostMapping("/add-category-child")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addCategoryChild(@ModelAttribute("categoryChildRequest") @Valid CategoryChildRequest categoryChildRequest,
                                         BindingResult bindingResult, RedirectAttributes redirectAttributes) {
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
            modelAndView.addObject("categoryChildRequest", new CategoryChildRequest());
            return modelAndView;
        }
        categoryService.saveCategoryChild(categoryChildRequest);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm danh mục con thành công!");
        modelAndView.setViewName("redirect:/category/add-category-child");
        return modelAndView;
    }

    @GetMapping("/list-category-parent")
    @PreAuthorize("hasRole('ADMIN')")
    public String listCategoriesParent(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoryPage = categoryService.findAllCategoryParent(pageable);
        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categoryPage.getTotalPages());
        return "category/list-category-parent";
    }

    @GetMapping("/list-category")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView listCategories(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        ModelAndView modelAndView = new ModelAndView();
        Pageable pageable = PageRequest.of(page, size);
        List<Category> categoriesParent = categoryService.findAllCategoryParent();
        Page<Category> categoryPage = categoryService.findAllCategoryChild(pageable);
        modelAndView.addObject("categoryParent", categoriesParent);
        modelAndView.addObject("categories", categoryPage.getContent());
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", categoryPage.getTotalPages());
        modelAndView.setViewName("category/list-category");
        return modelAndView;
    }

    @GetMapping("/update-category-parent/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView ShowUpdateCategoryParent(@PathVariable("id") String id) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<Category> categoryOptional = categoryService.findById(id);
        Category category = categoryOptional.get();
        CategoryParentRequest categoryParentRequest = new CategoryParentRequest();
        categoryParentRequest.setName(category.getName());
        modelAndView.addObject("categoryParentRequest", categoryParentRequest);
        modelAndView.addObject("id", id);
        modelAndView.setViewName("/category/update-category-parent");
        return modelAndView;
    }

    @PostMapping("/update-category-parent-put/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView updateCategoryParent(@PathVariable("id") String id,
                                             @Valid @ModelAttribute("categoryParentRequest") CategoryParentRequest categoryParentRequest,
                                             BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errorMessage", bindingResult.getAllErrors().get(0).getDefaultMessage());
            modelAndView.setViewName("/category/update-category-parent");
            return modelAndView;
        }
        categoryService.updateCategoryParent(id, categoryParentRequest);
        modelAndView.addObject("successMessage","Cập nhật danh mục thành công!");
        modelAndView.setViewName("/category/update-category-parent");
        return modelAndView;
    }

    @GetMapping("/update-category-child/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView showUpdateCategoryChild(@PathVariable("id") String id) {
        ModelAndView modelAndView = new ModelAndView();

        List<Category> parentCategories = categoryService.findAllCategoryParent();
        List<Attribute> attributes = attributeService.findAll();

        Optional<Category> categoryOptional = categoryService.findById(id);
        Category category = categoryOptional.get();
        CategoryChildRequest categoryChildRequest = new CategoryChildRequest();
        categoryChildRequest.setName(category.getName());
        categoryChildRequest.setParent(category.getParent());
        modelAndView.addObject("categoryChildRequest", categoryChildRequest);
        modelAndView.addObject("parentCategories", parentCategories);
        modelAndView.addObject("attributes", attributes);
        modelAndView.addObject("id", id);
        modelAndView.setViewName("/category/update-category-child");
        return modelAndView;
    }

    @PostMapping("/update-category-child/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView updateCategoryChild(@PathVariable("id") String id,
                                            @Valid @ModelAttribute("categoryChildRequest") CategoryChildRequest categoryChildRequest,
                                            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        List<Category> parentCategories = categoryService.findAllCategoryParent();
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("categoryChildRequest", categoryChildRequest);
            modelAndView.addObject("parentCategories", parentCategories);
            modelAndView.setViewName("redirect:/category/update-category-child");
            return modelAndView;
        }
        categoryService.updateCategoryChild(id, categoryChildRequest);
        redirectAttributes.addFlashAttribute("successMessage","Cập nhật danh mục thành công!");
        modelAndView.addObject("parentCategories", parentCategories);
        modelAndView.setViewName("redirect:/category/update-category-child/" + id);
        return modelAndView;
    }

    @GetMapping("/detail-category-child/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView detailCategoryChild(@PathVariable("id") String id) {
        ModelAndView modelAndView = new ModelAndView();
        List<Category> parentCategories = categoryService.findAllCategoryParent();
        Optional<Category> categoryOptional = categoryService.findById(id);
        Category category = categoryOptional.get();
        CategoryChildRequest categoryChildRequest = new CategoryChildRequest();
        categoryChildRequest.setName(category.getName());
        categoryChildRequest.setParent(category.getParent());
        modelAndView.addObject("categoryChildRequest", categoryChildRequest);
        modelAndView.addObject("parentCategories", parentCategories);
        modelAndView.addObject("id", id);
        modelAndView.setViewName("/category/detail-category-child");
        return modelAndView;
    }

    @GetMapping("/delete-category-parent/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView deleteCategoryParent(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        if (categoryRepository.existsByCategoryChildrenIsNotEmptyAndId(id)){
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không thể xóa!");
            modelAndView.setViewName("redirect:/category/list-category-parent");
            return modelAndView;
        }
        categoryService.deleteCategory(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa thành công!");
        modelAndView.setViewName("redirect:/category/list-category-parent");
        return modelAndView;
    }

    @GetMapping("/delete-category-child/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView deleteCategoryChild(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        if (categoryRepository.existsByAttributesIsNotEmptyAndId(id)){
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không thể xóa!");
            modelAndView.setViewName("redirect:/category/list-category");
            return modelAndView;
        }
        categoryService.deleteCategory(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa thành công!");
        modelAndView.setViewName("redirect:/category/list-category");
        return modelAndView;
    }
}
