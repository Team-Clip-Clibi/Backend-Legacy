package com.clip.api.user.controller.dto;

public enum JobCategory {
    STUDENT("학생"),
    MANUFACTURING("제조업"),
    MEDICAL("의료업"),
    ART("예술계"),
    IT("IT"),
    SERVICE("서비스업"),
    SALES("판매업"),
    BUSINESS("사업"),
    POLITICS("정치"),
    ETC("기타");

    private String jobCategoryName;
    JobCategory(String jobCategoryName) {
        this.jobCategoryName = jobCategoryName;
    }
    public String getJobCategoryName() {
        return jobCategoryName;
    }
}
