package com.clip.user.repository;

import com.clip.user.entity.Job;
import com.clip.user.entity.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserJobRepository extends JpaRepository<Job, Long> {

    @Query("SELECT j " +
            "FROM Job j " +
            "WHERE j.jobCategory = :jobCategory")
    Optional<Job> findJob(JobCategory jobCategory);
}
