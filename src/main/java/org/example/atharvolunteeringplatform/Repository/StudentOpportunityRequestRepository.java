package org.example.atharvolunteeringplatform.Repository;

import org.example.atharvolunteeringplatform.Model.Opportunity;
import org.example.atharvolunteeringplatform.Model.Student;
import org.example.atharvolunteeringplatform.Model.StudentOpportunityRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentOpportunityRequestRepository extends JpaRepository<StudentOpportunityRequest,Integer> {

    StudentOpportunityRequest findStudentOpportunityRequestById(Integer id);

//***
    StudentOpportunityRequest findByStudentAndOpportunity(Student student,Opportunity opportunity);

//***
    List<StudentOpportunityRequest>findAllByStudentId(Integer studentId);


    @Query("SELECT r FROM StudentOpportunityRequest r WHERE r.opportunity.organization.id = ?1 AND r.status = 'Completed'")
    List<StudentOpportunityRequest> findCompletedByOrganizationId(Integer organizationId);


    //10
    List<StudentOpportunityRequest> findByStudentIdAndStatus(Integer studentId, String status);

    boolean existsByStudentId(Integer studentId);

    //41
    //تقيم الفرصه من شرف النشاط
    // التأكد من وجود طالب من المدرسة قام بفرصة وحالتها منجزة
    @Query("SELECT r FROM StudentOpportunityRequest r WHERE r.opportunity.id = :opportunityId AND r.status = 'completed' AND r.student.school.id = :schoolId")
    List<StudentOpportunityRequest> findCompletedRequestsForOpportunity(Integer opportunityId, Integer schoolId);
}
