package com.clip.matching.repository;

import com.clip.matching.entity.RandomMatching;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RandomMatchingRepository extends JpaRepository<RandomMatching, Long> {
}
