package com.reactpro.react.repository;

import com.reactpro.react.dto.EmployeeSkillDTO;
import com.reactpro.react.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {


    Optional<Employee> findByEmployeeEmailAndPassword(String email, String password);

    List<Employee> findByManager(Employee manager);

    List<Employee> findByManager_Id(Long managerId);

    @Query("SELECT e FROM Employee e LEFT JOIN EmployeeSkill es ON e.id = es.employee.id LEFT JOIN Skill s ON es.skill.id = s.id LEFT JOIN Rating r ON es.rating.id = r.id")
    List<Employee> findAllWithSkillsAndRatings();
}
