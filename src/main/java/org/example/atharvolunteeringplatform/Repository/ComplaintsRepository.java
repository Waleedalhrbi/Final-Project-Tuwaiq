package org.example.atharvolunteeringplatform.Repository;

//import com.example.final_project.Model.Complaint;
import org.example.atharvolunteeringplatform.Model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ComplaintsRepository extends JpaRepository<Complaint, Integer> {

    Complaint findComplaintsById(Integer id);

 
    @Query("SELECT c FROM Complaint c WHERE c.students.id = ?1 AND c.createAt >= ?2 AND c.createAt <= ?3")
    List<Complaint> findByStudentIdAndCreateAtBetween(Integer studentId, LocalDateTime from, LocalDateTime to);
 
    @Query("SELECT c FROM Complaint c WHERE c.createAt >= :from AND c.createAt <= :to")
    List<Complaint> findComplaintsByCreateAtBetweenDates(LocalDateTime from, LocalDateTime to);


    @Query("SELECT c FROM Complaint c WHERE c.students.id = :studentId AND c.status = :status")
    List<Complaint> findByStudentIdAndStatus(Integer studentId, String status);

 

}
