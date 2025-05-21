package com.clip.user.service;

import com.clip.user.entity.Job;
import com.clip.user.entity.JobCategory;
import com.clip.user.repository.UserJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobService {
    private final UserJobRepository jobRepository;

    public Job findJob(JobCategory jobCategory) {
        return jobRepository.findJob(jobCategory).orElseThrow(IllegalAccessError::new);
    }
}
