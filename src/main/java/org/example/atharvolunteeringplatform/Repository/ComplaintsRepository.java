package org.example.atharvolunteeringplatform.Repository;

//import com.example.final_project.Model.Complaint;
import org.example.atharvolunteeringplatform.Model.Complaint;
import org.example.atharvolunteeringplatform.Model.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ComplaintsRepository extends JpaRepository<Complaint, Integer> {

    Complaint findComplaintsById(Integer id);

    List<Complaint> findComplaintsByCreateAtDateBetween(LocalDate from, LocalDate to);


    //12
    List<Complaint> findByStudentsId(Integer studentId);


}
