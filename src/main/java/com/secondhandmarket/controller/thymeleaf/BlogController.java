package com.secondhandmarket.controller.thymeleaf;

import com.secondhandmarket.model.Attribute;
import com.secondhandmarket.model.Product;
import com.secondhandmarket.service.blog.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/blog")

public class BlogController {
    private final BlogService blogService;

    @GetMapping("/list-blog")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView listBlogPending(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        ModelAndView modelAndView = new ModelAndView();
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> blogPage = blogService.findAllProductPending(pageable);
        modelAndView.addObject("blogs", blogPage);
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", blogPage.getTotalPages());
        modelAndView.setViewName("blog/list-blog");
        return modelAndView;
    }

    @GetMapping("/list-available-blog")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView listBlogAvailable(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        ModelAndView modelAndView = new ModelAndView();
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> blogPage = blogService.findAllProductAvailable(pageable);
        modelAndView.addObject("blogs", blogPage);
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", blogPage.getTotalPages());
        modelAndView.setViewName("blog/list-available-blog");
        return modelAndView;
    }

    @GetMapping("/list-rejected-blog")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView listBlogRejected(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        ModelAndView modelAndView = new ModelAndView();
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> blogPage = blogService.findAllProductRejected(pageable);
        modelAndView.addObject("blogs", blogPage);
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", blogPage.getTotalPages());
        modelAndView.setViewName("blog/list-rejected-blog");
        return modelAndView;
    }

    @PostMapping("/update-status-available/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView updateProductAvailable(@PathVariable("id") String id, RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView();
        blogService.updateProductAvailable(id);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công");
        modelAndView.setViewName("redirect:/blog/list-blog");
        return modelAndView;
    }

    @PostMapping("/update-status-remove/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView updateProductRemove(@PathVariable("id") String id, RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView();
        blogService.updateProductRemove(id);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công");
        modelAndView.setViewName("redirect:/blog/list-blog");
        return modelAndView;
    }

    @GetMapping("/detail-blog/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView productDetails(@PathVariable("id") String id){
        ModelAndView modelAndView = new ModelAndView();
        Product product = blogService.getProduct(id);
        modelAndView.addObject("product", product);
        modelAndView.setViewName("/blog/detail-blog");
        return modelAndView;
    }
}
