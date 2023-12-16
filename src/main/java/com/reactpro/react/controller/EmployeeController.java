package com.reactpro.react.controller;

import com.reactpro.react.dto.*;
import com.reactpro.react.model.Employee;
import com.reactpro.react.model.EmployeeSkill;
import com.reactpro.react.model.Rating;
import com.reactpro.react.model.Team;
import com.reactpro.react.repository.EmployeeRepository;
import com.reactpro.react.repository.EmployeeSkillRepository;
import com.reactpro.react.repository.RatingRepository;
import com.reactpro.react.repository.TeamRepository;
import com.reactpro.react.service.EmployeeService;
import com.reactpro.react.service.EmployeeSkillService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;



    @Autowired
    private EmployeeSkillRepository employeeSkillRepository;


    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EmployeeSkillService employeeSkillService;

    @Autowired
    private EmployeeRepository employeeRepository;



    @PostMapping("/signup")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        Employee savedEmployee = employeeService.addEmployee(employee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }



    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> authenticateUser(@RequestBody Employee loginRequest) {
        try {
            ResponseEntity<Map<String, Object>> authenticationResponse = employeeService.authenticateUser(loginRequest.getEmployeeEmail(), loginRequest.getPassword());

            // Check if the authentication was successful
            if (authenticationResponse.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> response = authenticationResponse.getBody();

                // Extract team information
                Team team = (Team) response.get("team");

                // Redirect based on the team
                if (team != null && ("HR".equalsIgnoreCase(team.getTeamName()) || "Leadership".equalsIgnoreCase(team.getTeamName()))) {
                    // Redirect to AllEmployeeDetails Page
                    response.put("redirect", "/all-employees");
                } else {
                    // Redirect to EmployeePersonalPage
                    response.put("redirect", "/employee-personal-page");
                }

                return ResponseEntity.ok(response);
            } else {
                // Authentication failed
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Authentication failed"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Internal server error"));
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        // Update fields
        existingEmployee.setEmployeeName(employee.getEmployeeName());
        existingEmployee.setEmployeeEmail(employee.getEmployeeEmail());
        existingEmployee.setPassword(employee.getPassword());

        // Set the manager based on the provided manager ID (assuming manager ID is an employee ID)
        Employee manager = employeeRepository.findById(employee.getManager().getId())
                .orElseThrow(() -> new EntityNotFoundException("Manager not found"));
        existingEmployee.setManager(manager);

        // Fetch the Team entity based on the provided team ID and set it to the employee
        Team team = teamRepository.findById(employee.getTeam().getId())
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));
        existingEmployee.setTeam(team);

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }



    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDetailsDTO> getEmployee(@PathVariable Long id) {
        EmployeeDetailsDTO employeeDetailsDTO = employeeService.getEmployee(id);
        return new ResponseEntity<>(employeeDetailsDTO, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<EmployeeDetailsDTO>> getAllEmployees() {
        List<EmployeeDetailsDTO> employeeDetailsDTOList = employeeService.getAllEmployees();
        return ResponseEntity.ok(employeeDetailsDTOList);
    }



    @GetMapping("/filter")
    public ResponseEntity<List<EmployeeDetailsDTO>> filterEmployeesBySkillsAndRatings(@RequestParam Map<String, String> skillsAndRatings) {
        // Convert String values to Integer
        Map<String, Integer> skillsAndRatingsInt = skillsAndRatings.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                    try {
                        return Integer.parseInt(entry.getValue());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return 0;
                    }
                }));

        List<EmployeeDetailsDTO> filteredEmployees = employeeSkillService.filterEmployeesBySkillsAndRatings(skillsAndRatingsInt);

        if (filteredEmployees.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(filteredEmployees, HttpStatus.OK);
        }
    }



    @GetMapping("/details/{id}")
    public ResponseEntity<Map<String, Object>> getAllEmployeeDetailsById(@PathVariable Long id) {
        Map<String, Object> employeeDetails = employeeService.getEmployeeDetailsById(id);
        return new ResponseEntity<>(employeeDetails, HttpStatus.OK);
    }

    @GetMapping("/employeesUnderManager/{managerId}")
    public ResponseEntity<List<Map<String, Object>>> getEmployeesUnderManager(@PathVariable Long managerId) {
        List<Map<String, Object>> employeesUnderManager = employeeService.getEmployeesUnderManager(managerId);
        return new ResponseEntity<>(employeesUnderManager, HttpStatus.OK);
    }



    @GetMapping("/all-employees-skill-rating")
    public ResponseEntity<List<Map<String, Object>>> getAllEmployeesSkillRating() {
        List<Map<String, Object>> employeesDetails = employeeService.getAllEmployeesDetails();
        return new ResponseEntity<>(employeesDetails, HttpStatus.OK);
    }


    @GetMapping("/all-ratings")
    public List<Rating> getAllRatings(){
        return employeeService.getAllRatings();
    }


}