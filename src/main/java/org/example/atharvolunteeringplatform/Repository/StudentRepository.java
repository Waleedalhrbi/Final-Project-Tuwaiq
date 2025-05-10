package org.example.atharvolunteeringplatform.Repository;

import org.example.atharvolunteeringplatform.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {

    Student findStudentById(Integer id);

    @Query("SELECT s FROM Student s WHERE s.grade_level = ?1 AND s.school.id = ?2 AND s.total_hours = 0")
    List<Student> findNonVolunteersStudents(String gradeLevel, Integer schoolId);



}
