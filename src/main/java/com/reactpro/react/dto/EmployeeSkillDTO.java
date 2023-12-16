package com.reactpro.react.dto;

public class EmployeeSkillDTO {

    private Long employeeId;
    private String employeeName;
    private String skillName;
    private int rating;

    public EmployeeSkillDTO(Long employeeId, String employeeName, String skillName, int rating) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.skillName = skillName;
        this.rating = rating;
    }

    public EmployeeSkillDTO() {
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
