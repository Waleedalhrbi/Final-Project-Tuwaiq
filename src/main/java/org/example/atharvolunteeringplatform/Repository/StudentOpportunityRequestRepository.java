package org.example.atharvolunteeringplatform.Repository;

import org.example.atharvolunteeringplatform.Model.Student;
import org.example.atharvolunteeringplatform.Model.StudentOpportunityRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentOpportunityRequestRepository extends JpaRepository<StudentOpportunityRequest,Integer> {

    StudentOpportunityRequest findStudentOpportunityRequestById(Integer id);


    @Query("SELECT r FROM StudentOpportunityRequest r WHERE r.opportunity.organization.id = ?1 AND r.status = 'Completed'")
    List<StudentOpportunityRequest> findCompletedByOrganizationId(Integer organizationId);


    List<StudentOpportunityRequest> findAllByStudent(Student student);

    @Query("SELECT r FROM StudentOpportunityRequest r WHERE r.student.id = ?1 AND LOWER(r.status) = 'completed'")
    List<StudentOpportunityRequest> findCompletedOpportunitiesByStudentId(Integer studentId);
}
