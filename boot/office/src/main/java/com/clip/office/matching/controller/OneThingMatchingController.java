package com.clip.office.matching.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.clip.office.matching.controller.dto.CreateOneThingMatchingDto;
import com.clip.office.matching.controller.dto.UpdateOneThingMatchingDto;
import com.clip.office.matching.service.OneThingMatchingService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/office/admin/home/oneThingMatching")
public class OneThingMatchingController {

    private final OneThingMatchingService oneThingMatchingService;

    @GetMapping("/create")
    public String createOneThingMatching(Model model) {
        model.addAttribute("createOneThingMatchingDto", new CreateOneThingMatchingDto());
        return "/office/admin/home";
    }

    @PostMapping("/create")
    public String createOneThingMatching(
        @ModelAttribute CreateOneThingMatchingDto createOneThingMatchingDto,
        RedirectAttributes redirectAttributes
    ) {
        oneThingMatchingService.createOneThingMatching(createOneThingMatchingDto);
        redirectAttributes.addFlashAttribute("successMessage", "원띵매칭이 성공적으로 생성되었습니다.");
        return "redirect:/office/admin/home";
    }

    @GetMapping("/update/{oneThingMatchingId}")
    public String updateOneThingMatchingForm(
            @PathVariable(value = "oneThingMatchingId") Long oneThingMatchingId,
            Model model
    ){
        UpdateOneThingMatchingDto updateDto = oneThingMatchingService.getUpdateOneThingMatchingDto(oneThingMatchingId);
        model.addAttribute("updateoneThingMatchingDto", updateDto);
        model.addAttribute("oneThingMatchingId", oneThingMatchingId);
        return "/office/admin/home/oneThingMatching/edit";
    }

    @PostMapping("/update/{oneThingMatchingId}")
    public String updateoneThingMatching(
            @PathVariable(value = "oneThingMatchingId") Long oneThingMatchingId,
            @ModelAttribute UpdateOneThingMatchingDto updateOneThingMatchingDto,
            RedirectAttributes redirectAttributes
    ){
        oneThingMatchingService.updateOneThingMatching(oneThingMatchingId, updateOneThingMatchingDto);
        redirectAttributes.addFlashAttribute("successMessage", "원띵매칭이 성공적으로 수정되었습니다.");
        return "redirect:/office/admin/home";
    }

    @PostMapping("/delete/{oneThingMatchingId}")
    public String deleteoneThingMatching(
            @PathVariable(value = "oneThingMatchingId") Long oneThingMatchingId,
            RedirectAttributes redirectAttributes
    ){
        oneThingMatchingService.deleteOneThingMatching(oneThingMatchingId);
        redirectAttributes.addFlashAttribute("successMessage", "원띵 매칭이 성공적으로 삭제되었습니다.");
        return "redirect:/office/admin/home";
    }
}
