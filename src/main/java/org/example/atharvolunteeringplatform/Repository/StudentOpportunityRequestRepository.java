package org.example.atharvolunteeringplatform.Repository;

import org.example.atharvolunteeringplatform.Model.Opportunity;
import org.example.atharvolunteeringplatform.Model.Student;
import org.example.atharvolunteeringplatform.Model.StudentOpportunityRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentOpportunityRequestRepository extends JpaRepository<StudentOpportunityRequest,Integer> {

    StudentOpportunityRequest findStudentOpportunityRequestById(Integer id);

//***
    StudentOpportunityRequest findByStudentAndOpportunity(Student student,Opportunity opportunity);


}
