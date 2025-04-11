package com.clip.office.notice.service;

import com.clip.notice.entity.News;
import com.clip.notice.service.NewsDataService;
import com.clip.office.notice.controller.dto.CreateNewsDto;
import com.clip.office.notice.controller.dto.UpdateNewsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsDataService newsDataService;

    @Transactional
    public CreateNewsDto createNews(CreateNewsDto createNewsDto) {

        News news = News.builder()
                .content(createNewsDto.getContent())
                .link(createNewsDto.getLink())
                .exposureDate(createNewsDto.getExposureDate())
                .isExposure(false)
                .build();

        newsDataService.save(news);
        return CreateNewsDto.builder()
                .id(news.getId())
                .content(news.getContent())
                .link(news.getLink())
                .exposureDate(news.getExposureDate())
                .build();
    }

    @Transactional
    public UpdateNewsDto updateNews(Long newsId, UpdateNewsDto updateNewsDto) {
        News news = newsDataService.findNews(newsId);

        news.updateNews(
                updateNewsDto.getContent(),
                updateNewsDto.getLink(),
                updateNewsDto.getExposureDate(),
                updateNewsDto.isExposure()
        );

        return UpdateNewsDto.builder()
                .id(news.getId())
                .content(news.getContent())
                .link(news.getLink())
                .exposureDate(news.getExposureDate())
                .isExposure(news.isExposure())
                .build();
    }

    public void deleteNews(Long newsId) {
        newsDataService.deleteNews(newsId);
    }
}
