package com.reactpro.react.dto;

import java.util.List;

public class EmployeeDTO {

    private Long id;
    private String employeeName;
    private List<SkillRatingDTO> skills;

    public EmployeeDTO(Long id, String employeeName, List<SkillRatingDTO> skills) {
        this.id = id;
        this.employeeName = employeeName;
        this.skills = skills;
    }

    public EmployeeDTO(Long id, String employeeName) {
        this.id = id;
        this.employeeName = employeeName;
    }

    public EmployeeDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public List<SkillRatingDTO> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillRatingDTO> skills) {
        this.skills = skills;
    }
}
