package com.reactpro.react.service;

import com.reactpro.react.dto.EmployeeDTO;
import com.reactpro.react.dto.EmployeeDetailsDTO;
import com.reactpro.react.dto.ManagerDTO;
import com.reactpro.react.dto.SkillRatingDTO;
import com.reactpro.react.model.Employee;
import com.reactpro.react.model.EmployeeSkill;
import com.reactpro.react.model.Rating;
import com.reactpro.react.model.Skill;
import com.reactpro.react.repository.EmployeeRepository;
import com.reactpro.react.repository.EmployeeSkillRepository;
import com.reactpro.react.repository.RatingRepository;
import com.reactpro.react.repository.SkillRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class EmployeeSkillService {

    @Autowired
    private EmployeeSkillRepository employeeSkillRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private RatingRepository ratingRepository;



    public ResponseEntity<String> addEmployeeSkill(EmployeeSkill employeeSkill) {
        // to check if the skill with the same name exists for the employee
        Optional<EmployeeSkill> existingSkill = employeeSkillRepository
                .findByEmployee_IdAndSkill_Id(
                        employeeSkill.getEmployee().getId(),
                        employeeSkill.getSkill().getId()
                );

        if (existingSkill.isPresent()) {
            return new ResponseEntity<>("Skill with the same name already exists for the employee", HttpStatus.BAD_REQUEST);
        }

        // if the skill doesn't exist, it will add it
        employeeSkillRepository.save(employeeSkill);

        return new ResponseEntity<>("EmployeeSkill added successfully", HttpStatus.CREATED);
    }



    public void updateEmployeeSkill(Long employeeId, Long skillId, int ratingValue) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow();

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow();

        Rating rating = ratingRepository.findByRating(ratingValue)
                .orElseGet(() -> ratingRepository.save(new Rating(ratingValue)));

        Optional<EmployeeSkill> optionalEmployeeSkill = employeeSkillRepository.findByEmployeeAndSkill(employee, skill);
        if (optionalEmployeeSkill.isPresent()) {
            EmployeeSkill existingEmployeeSkill = optionalEmployeeSkill.get();
            existingEmployeeSkill.setRating(rating);

            employeeSkillRepository.save(existingEmployeeSkill);
        } else {
        }
    }



    public ResponseEntity<EmployeeDTO> getEmployeeSkills(Long employeeId) {
        List<EmployeeSkill> employeeSkills = employeeSkillRepository.findByEmployee_Id(employeeId);

        if (employeeSkills.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Employee employee = employeeSkills.get(0).getEmployee(); // Assuming there's at least one skill
        EmployeeDTO employeeDTO = new EmployeeDTO(employee.getId(), employee.getEmployeeName());

        List<SkillRatingDTO> skills = employeeSkills.stream()
                .map(employeeSkill -> new SkillRatingDTO(
                        employeeSkill.getSkill().getSkillName(),
                        employeeSkill.getRating().getRating()
                ))
                .collect(Collectors.toList());

        employeeDTO.setSkills(skills);

        return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
    }


    public List<EmployeeDetailsDTO> findEmployeesBySkillsAndRating(String skillName, int minRating) {
        List<EmployeeSkill> employeeSkills = employeeSkillRepository.findBySkill_SkillNameAndRating_RatingGreaterThanEqual(skillName, minRating);

        List<Employee> uniqueEmployees = employeeSkills.stream()
                .map(EmployeeSkill::getEmployee)
                .distinct()
                .collect(Collectors.toList());

        return uniqueEmployees.stream()
                .map(this::convertToEmployeeDetailsDTO)
                .collect(Collectors.toList());
    }

    private EmployeeDetailsDTO convertToEmployeeDetailsDTO(Employee employee) {
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

        return employeeDetailsDTO;
    }





    public List<EmployeeDetailsDTO> filterEmployeesBySkillsAndRatings(Map<String, Integer> skillsAndRatings) {
        List<EmployeeDetailsDTO> filteredEmployees = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : skillsAndRatings.entrySet()) {
            String skillName = entry.getKey();
            int minRating = entry.getValue();

            try {
                List<EmployeeSkill> employeeSkills = employeeSkillRepository
                        .findBySkill_SkillNameAndRating_RatingGreaterThanEqual(skillName, minRating);

                List<Employee> uniqueEmployees = employeeSkills.stream()
                        .map(EmployeeSkill::getEmployee)
                        .distinct()
                        .collect(Collectors.toList());

                List<EmployeeDetailsDTO> employeesForSkill = uniqueEmployees.stream()
                        .map(this::convertToEmployeeDetailsDTO)
                        .collect(Collectors.toList());

                filteredEmployees.addAll(employeesForSkill);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return filteredEmployees;
    }




    @Transactional
    public void deleteEmployeeSkill(Long employeeId, Long skillId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + employeeId));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new EntityNotFoundException("Skill not found with id: " + skillId));

        employeeSkillRepository.deleteByEmployeeAndSkill(employee, skill);
    }


    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }


    public List<Employee> getAllEmployeesWithSkillsAndRatings() {
        return employeeRepository.findAllWithSkillsAndRatings();
    }


    public EmployeeSkill updateEmployeeSkillRating(Long employeeId, Long skillId, Long ratingId) {
        EmployeeSkill employeeSkill = employeeSkillRepository.findByEmployeeIdAndSkillId(employeeId, skillId);
        if (employeeSkill != null) {
            Optional<Rating> optionalRating = ratingRepository.findById(ratingId);
            if (optionalRating.isPresent()) {
                Rating rating = optionalRating.get();
                employeeSkill.setRating(rating);
                return employeeSkillRepository.save(employeeSkill);
            } else {
                throw new RuntimeException("Rating with ID " + ratingId + " not found");
            }
        }
        return null;
    }



}


