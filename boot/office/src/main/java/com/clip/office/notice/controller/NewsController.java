package com.clip.office.notice.controller;

import com.clip.office.notice.controller.dto.CreateNewsDto;
import com.clip.office.notice.controller.dto.UpdateNewsDto;
import com.clip.office.notice.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/office/notice/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping("/create")
    public ResponseEntity<CreateNewsDto> createNews(
            @RequestBody CreateNewsDto createNewsDto
    ) {
        return ResponseEntity.ok(newsService.createNews(createNewsDto));
    }

    @PutMapping("/{newsId}/update")
    public ResponseEntity<UpdateNewsDto> updateNews(
            @PathVariable(value = "newsId") Long newsId,
            @RequestBody UpdateNewsDto updateNewsDto
    ) {
        return ResponseEntity.ok(newsService.updateNews(newsId, updateNewsDto));
    }

    @DeleteMapping("/{newsId}/delete")
    public ResponseEntity<Void> deleteNews(
            @PathVariable(value = "newsId") Long newsId
    ) {
        newsService.deleteNews(newsId);
        return ResponseEntity.ok().build();
    }
}
