package org.example.atharvolunteeringplatform.Repository;

//import com.example.final_project.Model.Complaints;
import org.example.atharvolunteeringplatform.Model.Complaints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ComplaintsRepository extends JpaRepository<Complaints, Integer> {

    Complaints findComplaintsById(Integer id);

    List<Complaints> findComplaintsByCreateAtDateBetween(LocalDate from, LocalDate to);


}
