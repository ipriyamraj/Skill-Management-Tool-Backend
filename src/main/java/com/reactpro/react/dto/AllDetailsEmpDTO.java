package com.reactpro.react.dto;

import com.reactpro.react.model.Team;

import java.util.List;

public class AllDetailsEmpDTO {

    private Long id;
    private String employeeName;
    private String employeeEmail;
    private ManagerDTO manager;
    private Team team;
    private List<SkillRatingDTO> skills;

    public AllDetailsEmpDTO(Long id, String employeeName, String employeeEmail, ManagerDTO manager, Team team, List<SkillRatingDTO> skills) {
        this.id = id;
        this.employeeName = employeeName;
        this.employeeEmail = employeeEmail;
        this.manager = manager;
        this.team = team;
        this.skills = skills;
    }

    public AllDetailsEmpDTO() {
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

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public ManagerDTO getManager() {
        return manager;
    }

    public void setManager(ManagerDTO manager) {
        this.manager = manager;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<SkillRatingDTO> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillRatingDTO> skills) {
        this.skills = skills;
    }
}
