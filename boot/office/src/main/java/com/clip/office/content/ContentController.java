//package com.clip.office.content;
//
//import java.util.List;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import com.clip.matching.entity.OneThingMatching;
//import com.clip.matching.entity.RandomMatching;
//import com.clip.office.matching.controller.dto.CreateOneThingMatchingDto;
//import com.clip.office.matching.controller.dto.CreateRandomMatchingDto;
//import com.clip.office.matching.service.OneThingMatchingService;
//import com.clip.office.matching.service.RandomMatchingService;
//
//import lombok.RequiredArgsConstructor;
//
//@Controller
//@RequestMapping("/office/admin/home")
//@RequiredArgsConstructor
//public class ContentController {
//
//    private final OneThingMatchingService onethingMatchingService;
//    private final RandomMatchingService randomMatchingService;
//
//    @GetMapping("/oneThingMatching")
//    public String oneThingMatchingList(Model model) {
//        List<OneThingMatching> oneThingMatchingList = onethingMatchingService.findAllOneThingMatchings();
//        model.addAttribute("oneThingMatchings", oneThingMatchingList);
//        model.addAttribute("createOneThingMatchingDto", new CreateOneThingMatchingDto());
//        model.addAttribute("isOnething", true);
//        return "fragments/content :: #dynamicContent";
//    }
//
//    @GetMapping("/randomMatching")
//    public String randomMatchingList(Model model) {
//        List<RandomMatching> randomMatchingList = randomMatchingService.findAllRandomMatchings();
//        model.addAttribute("randomMatchings", randomMatchingList);
//        model.addAttribute("createRandomMatchingDto", new CreateRandomMatchingDto());
//        model.addAttribute("isOnething", false);
//        return "fragments/content :: #dynamicContent";
//    }
//}
