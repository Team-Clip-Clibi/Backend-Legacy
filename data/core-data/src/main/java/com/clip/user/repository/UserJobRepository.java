package com.clip.user.repository;

import com.clip.user.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJobRepository extends JpaRepository<Job, Long> {}
