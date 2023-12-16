package com.reactpro.react.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reactpro.react.dto.EmployeeDTO;
import com.reactpro.react.model.Employee;
import com.reactpro.react.model.EmployeeSkill;
import com.reactpro.react.model.Skill;
import com.reactpro.react.repository.EmployeeSkillRepository;
import com.reactpro.react.service.EmployeeService;
import com.reactpro.react.service.EmployeeSkillService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:3000/")

@RestController
@RequestMapping("/api/employee-skills")
public class EmployeeSkillController {

    @Autowired
    private EmployeeSkillRepository employeeSkillRepository;

    @Autowired
    private EmployeeSkillService employeeSkillService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<String> addEmployeeSkill(@RequestBody EmployeeSkill employeeSkill) {
        ResponseEntity<String> response = employeeSkillService.addEmployeeSkill(employeeSkill);

        if (response.getStatusCode().is2xxSuccessful()) {
            String successMessage = response.getBody();
            System.out.println(successMessage);
        } else {
            System.out.println("Failed to add skills and ratings");
        }

        return response;
    }



    @PutMapping("/{employeeId}/{skillId}")
    public ResponseEntity<String> updateEmployeeSkill(
            @PathVariable Long employeeId,
            @PathVariable Long skillId,
            @RequestParam int ratingValue) {

        try {
            employeeSkillService.updateEmployeeSkill(employeeId, skillId, ratingValue);
            return new ResponseEntity<>("EmployeeSkill updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception or handle as needed
            return new ResponseEntity<>("Failed to update EmployeeSkill", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployeeSkills(@PathVariable Long employeeId) {
        ResponseEntity<EmployeeDTO> employeeSkillsResponse = employeeSkillService.getEmployeeSkills(employeeId);

        if (employeeSkillsResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(employeeSkillsResponse.getBody(), HttpStatus.OK);
        } else {
            // returning the original response if not successful
            return new ResponseEntity<>(employeeSkillsResponse.getStatusCode());
        }
    }


    @DeleteMapping("/{employeeId}/{skillId}")
    public ResponseEntity<String> deleteEmployeeSkill(@PathVariable Long employeeId, @PathVariable Long skillId) {
        employeeSkillService.deleteEmployeeSkill(employeeId, skillId);
        return new ResponseEntity<>("EmployeeSkill deleted successfully", HttpStatus.OK);
    }


    @GetMapping("/all-skills")
    public List<Skill> getAllSkills() {
        return employeeSkillService.getAllSkills();
    }


    @PutMapping("/updateRating/{employeeId}/{skillId}")
    public ResponseEntity<String> updateEmployeeSkillRating(
            @PathVariable Long employeeId,
            @PathVariable Long skillId,
            @RequestParam Long ratingId
    ) {
        try {
            employeeSkillService.updateEmployeeSkillRating(employeeId, skillId, ratingId);
            return ResponseEntity.ok("Rating updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Rating not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating rating: " + e.getMessage());
        }
    }



}

