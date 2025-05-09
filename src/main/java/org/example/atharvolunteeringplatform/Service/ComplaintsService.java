package org.example.atharvolunteeringplatform.Service;
import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Api.ApiException;
import org.example.atharvolunteeringplatform.Model.Complaint;
import org.example.atharvolunteeringplatform.Repository.ComplaintsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintsService {


    private final ComplaintsRepository complaintsRepository;

    public List<Complaint> getAllComplaints() {
        return complaintsRepository.findAll();
    }

    public void addComplaint(Complaint complaint) {
        complaint.setCreateAt(LocalDateTime.now());
        complaint.setStatus("In Progress");
        complaintsRepository.save(complaint);
    }

    public void updateComplaint(Integer id, Complaint updatedComplaint) {
        Complaint complaint =complaintsRepository.findComplaintsById(id);
        if(complaint ==null) {
            throw new ApiException("Complaint not found");}

        complaint.setTitle(updatedComplaint.getTitle());
        complaint.setDescription(updatedComplaint.getDescription());
        complaint.setStatus(updatedComplaint.getStatus());
        complaintsRepository.save(complaint);
    }

    public void deleteComplaint(Integer id) {
        Complaint complaint =complaintsRepository.findComplaintsById(id);
        if(complaint ==null) {
            throw new ApiException("Complaint not found");}
        complaintsRepository.delete(complaint);
    }


    //14
    public List<Complaint> getComplaintsByDateRange(LocalDate from, LocalDate to) {
        return complaintsRepository.findComplaintsByCreateAtDateBetween(from, to);
    }
}
