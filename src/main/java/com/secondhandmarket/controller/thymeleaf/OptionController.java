package com.secondhandmarket.controller.thymeleaf;

import com.secondhandmarket.dto.option.OptionRequest;
import com.secondhandmarket.model.Option;
import com.secondhandmarket.repository.OptionRepository;
import com.secondhandmarket.service.Option.OptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/option")


public class OptionController {
    private final OptionService optionService;
    private final OptionRepository optionRepository;

    @PostMapping("/add-option/{attributeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addOption(@ModelAttribute("optionRequest") @Valid OptionRequest request,
                                  @PathVariable("attributeId") String attributeId,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", bindingResult.getAllErrors().get(0).getDefaultMessage());
            redirectAttributes.addFlashAttribute("optionRequest", request);
            modelAndView.setViewName("redirect:/attribute/list-attribute");
            return modelAndView;
        }
        optionService.saveOption(request, attributeId);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm option thành công");
        modelAndView.setViewName("redirect:/attribute/list-attribute");

        return modelAndView;
    }


    @GetMapping("/list-option")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView listOption(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        ModelAndView modelAndView = new ModelAndView();
        Pageable pageable = PageRequest.of(page, size);
        Page<Option> options = optionRepository.findAll(pageable);
        modelAndView.addObject("options", options.getContent());
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", options.getTotalPages());
        modelAndView.setViewName("/option/list-option");
        return modelAndView;
    }

    @GetMapping("/update-option/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView showUpdateOption(@PathVariable("id") String id) {
        ModelAndView modelAndView = new ModelAndView();
        Option option = optionRepository.findById(id).get();
        OptionRequest optionRequest = new OptionRequest();
        optionRequest.setName(option.getName());
        modelAndView.addObject("optionRequest", optionRequest);
        modelAndView.setViewName("/option/update-option");
        return modelAndView;
    }

    @PostMapping("/update-option/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView updateOption(@PathVariable("id") String id,
                                     @ModelAttribute("optionRequest") @Valid OptionRequest request,
                                     BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errorMessage", bindingResult.getAllErrors().get(0).getDefaultMessage());
            modelAndView.addObject("optionRequest", new OptionRequest());
            modelAndView.setViewName("option/update-option");
            return modelAndView;
        }
        optionService.updateOption(id, request);
        redirectAttributes.addFlashAttribute("successMessage","Cập nhật option thành công");
        modelAndView.setViewName("redirect:/attribute/list-attribute");
        return modelAndView;
    }

    @GetMapping("/delete-option/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView deleteOption(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        optionRepository.deleteById(id);
        ModelAndView modelAndView = new ModelAndView();
        redirectAttributes.addFlashAttribute("successMessage", "Xóa option thành công!");
        modelAndView.setViewName("redirect:/attribute/list-attribute");
        return modelAndView;
    }
}
