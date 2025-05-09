package org.example.atharvolunteeringplatform.Repository;

//import com.example.final_project.Model.Complaint;
import org.example.atharvolunteeringplatform.Model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintsRepository extends JpaRepository<Complaint, Integer> {

    Complaint findComplaintsById(Integer id);

}
