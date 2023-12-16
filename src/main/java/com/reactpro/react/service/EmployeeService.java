package com.reactpro.react.service;

import com.reactpro.react.dto.EmployeeDetailsDTO;
import com.reactpro.react.dto.EmployeeSkillDTO;
import com.reactpro.react.dto.ManagerDTO;
import com.reactpro.react.model.*;
import com.reactpro.react.repository.EmployeeRepository;
import com.reactpro.react.repository.EmployeeSkillRepository;
import com.reactpro.react.repository.RatingRepository;
import com.reactpro.react.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EmployeeSkillRepository employeeSkillRepository;

    @Autowired
    private RatingRepository ratingRepository;


    public Employee addEmployee(Employee employee) {
        // Add any business logic or validation if needed
        return employeeRepository.save(employee);
    }

    public ResponseEntity<Map<String, Object>> authenticateUser(String email, String password) throws Exception {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmployeeEmailAndPassword(email, password);

        if (optionalEmployee.isPresent()) {
            Employee authenticatedEmployee = optionalEmployee.get();

            // Get team information
            Team team = authenticatedEmployee.getTeam();

            Map<String, Object> response = new HashMap<>();
            response.put("employee", authenticatedEmployee);
            response.put("team", team);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new Exception("Invalid email or password");
        }
    }


    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        existingEmployee.setEmployeeName(updatedEmployee.getEmployeeName());
        existingEmployee.setEmployeeEmail(updatedEmployee.getEmployeeEmail());
        existingEmployee.setPassword(updatedEmployee.getPassword());
        existingEmployee.setManager(updatedEmployee.getManager());
        existingEmployee.setTeam(updatedEmployee.getTeam());

        return employeeRepository.save(existingEmployee);
    }



    public EmployeeDetailsDTO getEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        EmployeeDetailsDTO employeeDetailsDTO = new EmployeeDetailsDTO();
        employeeDetailsDTO.setId(employee.getId());
        employeeDetailsDTO.setEmployeeName(employee.getEmployeeName());
        employeeDetailsDTO.setEmployeeEmail(employee.getEmployeeEmail());

        if (employee.getManager() != null) {
            ManagerDTO managerDTO = new ManagerDTO();
            managerDTO.setId(employee.getManager().getId());
            managerDTO.setEmployeeName(employee.getManager().getEmployeeName());
            employeeDetailsDTO.setManager(managerDTO);
        }

        employeeDetailsDTO.setTeam(employee.getTeam());

        return employeeDetailsDTO;
    }



    public List<EmployeeDetailsDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream()
                .map(this::convertToEmployeeDetailsDTO)
                .collect(Collectors.toList());
    }

    private EmployeeDetailsDTO convertToEmployeeDetailsDTO(Employee employee) {
        EmployeeDetailsDTO employeeDetailsDTO = new EmployeeDetailsDTO();
        employeeDetailsDTO.setId(employee.getId());
        employeeDetailsDTO.setEmployeeName(employee.getEmployeeName());
        employeeDetailsDTO.setEmployeeEmail(employee.getEmployeeEmail());

        if (employee.getManager() != null) {
            ManagerDTO managerDetailsDTO = new ManagerDTO();
            managerDetailsDTO.setId(employee.getManager().getId());
            managerDetailsDTO.setEmployeeName(employee.getManager().getEmployeeName());
            employeeDetailsDTO.setManager(managerDetailsDTO);
        }

        if (employee.getTeam() != null) {
            Team team = employee.getTeam();
            employeeDetailsDTO.setTeam(team);
        }

        return employeeDetailsDTO;
    }





//    public List<EmployeeDetailsDTO> filterEmployeesBySkillsAndRatings(Map<String, Integer> skillsAndRatings) {
//        return null;
//    }



    public Map<String, Object> getEmployeeDetailsById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        Map<String, Object> employeeDetails = new HashMap<>();

        employeeDetails.put("id", employee.getId());
        employeeDetails.put("employeeName", employee.getEmployeeName());
        employeeDetails.put("employeeEmail", employee.getEmployeeEmail());

        if (employee.getManager() != null) {
            Map<String, Object> managerDetails = new HashMap<>();
            managerDetails.put("id", employee.getManager().getId());
            managerDetails.put("employeeName", employee.getManager().getEmployeeName());
            employeeDetails.put("manager", managerDetails);
        }

        if (employee.getTeam() != null) {
            Map<String, Object> teamDetails = new HashMap<>();
            teamDetails.put("id", employee.getTeam().getId());
            teamDetails.put("teamName", employee.getTeam().getTeamName());
            employeeDetails.put("team", teamDetails);
        }

        List<EmployeeSkill> employeeSkills = employeeSkillRepository.findByEmployee_Id(id);
        if (!employeeSkills.isEmpty()) {
            Map<String, Integer> skillsAndRatings = new HashMap<>();
            for (EmployeeSkill employeeSkill : employeeSkills) {
                skillsAndRatings.put(employeeSkill.getSkill().getSkillName(), employeeSkill.getRating().getRating());
            }
            employeeDetails.put("skillsAndRatings", skillsAndRatings);
        }

        return employeeDetails;
    }


    public List<Map<String, Object>> getEmployeesUnderManager(Long managerId) {
        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new EntityNotFoundException("Manager not found"));

        List<Employee> employeesUnderManager = employeeRepository.findByManager_Id(managerId);

        List<Map<String, Object>> employeesDetailsUnderManager = new ArrayList<>();

        for (Employee employee : employeesUnderManager) {
            Map<String, Object> employeeDetails = getEmployeeDetails(employee);
            employeesDetailsUnderManager.add(employeeDetails);
        }

        return employeesDetailsUnderManager;
    }



    private Map<String, Object> getEmployeeDetails(Employee employee) {
        Map<String, Object> employeeDetails = new HashMap<>();
        employeeDetails.put("id", employee.getId());
        employeeDetails.put("employeeName", employee.getEmployeeName());
        employeeDetails.put("employeeEmail", employee.getEmployeeEmail());

        if (employee.getManager() != null) {
            Map<String, Object> managerDetails = new HashMap<>();
            managerDetails.put("id", employee.getManager().getId());
            managerDetails.put("employeeName", employee.getManager().getEmployeeName());
            employeeDetails.put("manager", managerDetails);
        }

        if (employee.getTeam() != null) {
            Map<String, Object> teamDetails = new HashMap<>();
            teamDetails.put("id", employee.getTeam().getId());
            teamDetails.put("teamName", employee.getTeam().getTeamName());
            employeeDetails.put("team", teamDetails);
        }

        List<EmployeeSkill> employeeSkills = employeeSkillRepository.findByEmployee_Id(employee.getId());
        if (!employeeSkills.isEmpty()) {
            Map<String, Integer> skillsAndRatings = new HashMap<>();
            for (EmployeeSkill employeeSkill : employeeSkills) {
                if (employeeSkill.getSkill() != null && employeeSkill.getRating() != null) {
                    skillsAndRatings.put(employeeSkill.getSkill().getSkillName(), employeeSkill.getRating().getRating());
                }
            }
            employeeDetails.put("skillsAndRatings", skillsAndRatings);
        }

        return employeeDetails;
    }




    public List<Map<String, Object>> getAllEmployeesDetails() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream()
                .map(this::getEmployeeDetails)
                .collect(Collectors.toList());
    }



    public List<Rating> getAllRatings(){
        return ratingRepository.findAll();
    }




}