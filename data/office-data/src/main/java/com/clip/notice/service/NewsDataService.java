package com.clip.notice.service;

import com.clip.notice.entity.News;
import com.clip.notice.exception.NewsNotFoundException;
import com.clip.notice.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsDataService {

    private final NewsRepository newsRepository;

    public News findNews(Long newsId) {
        return newsRepository.findNews(newsId)
                .orElseThrow(NewsNotFoundException::new);
    }

    public News save(News news) {
        return newsRepository.save(news);
    }

    public void deleteNews(Long newsId) {
        newsRepository.deleteNews(newsId);
    }
}
