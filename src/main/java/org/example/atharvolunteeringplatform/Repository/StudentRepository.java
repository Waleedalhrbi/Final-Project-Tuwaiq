package org.example.atharvolunteeringplatform.Repository;

import org.example.atharvolunteeringplatform.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {

    Student findStudentById(Integer id);

//39
//في صف دراسي معين مثل ثالث ثانوي و مطابق للقيمه الممره
//وتمت الموافقة على طلباتهم او  progress
    @Query("SELECT DISTINCT s FROM Student s JOIN s.studentOpportunityRequests r " + "WHERE s.grade_level = :grade AND r.status IN ('approved', 'progress','completed')")
    List<Student> findVolunteeringStudentsByGrade(String grade);
}
