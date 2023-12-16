package com.reactpro.react.repository;

import com.reactpro.react.model.Employee;
import com.reactpro.react.model.EmployeeSkill;
import com.reactpro.react.model.Skill;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EmployeeSkillRepository extends JpaRepository<EmployeeSkill, Long> {

    Optional<EmployeeSkill> findByEmployee_IdAndSkill_Id(Long employee_Id, Long skillId);

    List<EmployeeSkill> findByEmployee_Id(Long id);

    List<EmployeeSkill> findBySkill_SkillNameAndRating_RatingGreaterThanEqual(String skillName, int minRating);


    Optional<EmployeeSkill> findByEmployeeAndSkill(Employee employee, Skill skill);


    @Transactional
    @Modifying
    @Query("DELETE FROM EmployeeSkill e WHERE e.employee = :employee AND e.skill = :skill")
    void deleteByEmployeeAndSkill(@Param("employee") Employee employee, @Param("skill") Skill skill);

    List<EmployeeSkill> findByEmployee_IdAndSkill_SkillNameAndRating_RatingGreaterThanEqual(Long id, String skillName, int minRating);

    Optional<EmployeeSkill> findByEmployeeAndSkill_SkillNameAndRating_RatingGreaterThanEqual(Employee employee, String skillName, int minRating);

//    List<EmployeeSkill> findByEmployee_IdAndSkill_SkillNameAndRating_RatingGreaterThanEqual(Long id, String skillName, int minRating);

    EmployeeSkill findByEmployeeIdAndSkillId(Long employeeId, Long skillId);

}
