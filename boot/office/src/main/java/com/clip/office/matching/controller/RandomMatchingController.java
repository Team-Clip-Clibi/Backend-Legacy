package com.clip.office.matching.controller;

import com.clip.office.matching.controller.dto.CreateRandomMatchingDto;
import com.clip.office.matching.controller.dto.UpdateRandomMatchingDto;
import com.clip.office.matching.service.RandomMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/office/admin/home/randomMatching")
public class RandomMatchingController {

    private final RandomMatchingService randomMatchingService;


    @GetMapping("/create")
    public String createRandomMatchingForm(Model model) {
        model.addAttribute("createRandomMatchingDto", new CreateRandomMatchingDto());
        return "redirect:/office/admin/home";
    }

    @PostMapping("/create")
    public String createRandomMatching(
        @ModelAttribute CreateRandomMatchingDto createRandomMatchingDto,
        RedirectAttributes redirectAttributes
    ){
        randomMatchingService.createRandomMatching(createRandomMatchingDto);
        redirectAttributes.addFlashAttribute("successMessage", "랜덤 매칭이 성공적으로 생성되었습니다.");
        return "redirect:/office/admin/home";
    }

    @GetMapping("/update/{randomMatchingId}")
    public String updateRandomMatchingForm(
            @PathVariable(value = "randomMatchingId") Long randomMatchingId,
            Model model
    ){
        UpdateRandomMatchingDto updateDto = randomMatchingService.getUpdateRandomMatchingDto(randomMatchingId);
        model.addAttribute("updateRandomMatchingDto", updateDto);
        model.addAttribute("randomMatchingId", randomMatchingId);
        return "/office/admin/home/randomMatching/edit";
    }

    @PostMapping("/update/{randomMatchingId}")
    public String updateRandomMatching(
            @PathVariable(value = "randomMatchingId") Long randomMatchingId,
            @ModelAttribute UpdateRandomMatchingDto updateRandomMatchingDto,
            RedirectAttributes redirectAttributes
    ){
        randomMatchingService.updateRandomMatching(randomMatchingId, updateRandomMatchingDto);
        redirectAttributes.addFlashAttribute("successMessage", "랜덤 매칭이 성공적으로 수정되었습니다.");
        return "redirect:/office/admin/home";
    }

    @PostMapping("/delete/{randomMatchingId}")
    public String deleteRandomMatching(
            @PathVariable(value = "randomMatchingId") Long randomMatchingId,
            RedirectAttributes redirectAttributes
    ){
        randomMatchingService.deleteRandomMatching(randomMatchingId);
        redirectAttributes.addFlashAttribute("successMessage", "랜덤 매칭이 성공적으로 삭제되었습니다.");
        return "redirect:/office/admin/home";
    }
}
