package com.reactpro.react.dto;

import org.springframework.stereotype.Component;

public class ManagerDTO {

    private Long id;
    private String employeeName;

    public ManagerDTO(Long id, String employeeName) {
        this.id = id;
        this.employeeName = employeeName;
    }

    public ManagerDTO() {
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
}
