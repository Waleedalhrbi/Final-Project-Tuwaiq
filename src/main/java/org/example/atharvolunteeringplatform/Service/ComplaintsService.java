package org.example.atharvolunteeringplatform.Service;
import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Api.ApiException;
import org.example.atharvolunteeringplatform.Model.Complaints;
import org.example.atharvolunteeringplatform.Repository.ComplaintsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintsService {


    private final ComplaintsRepository complaintsRepository;

    public List<Complaints> getAllComplaints() {
        return complaintsRepository.findAll();
    }

    public void addComplaint(Complaints complaint) {
        complaint.setCreateAt(LocalDateTime.now());
        complaint.setStatus("In Progress");
        complaintsRepository.save(complaint);
    }

    public void updateComplaint(Integer id, Complaints updatedComplaint) {
        Complaints complaints=complaintsRepository.findComplaintsById(id);
        if(complaints==null) {
            throw new ApiException("Complaint not found");}

        complaints.setTitle(updatedComplaint.getTitle());
        complaints.setDescription(updatedComplaint.getDescription());
        complaints.setStatus(updatedComplaint.getStatus());
        complaintsRepository.save(complaints);
    }

    public void deleteComplaint(Integer id) {
        Complaints complaints=complaintsRepository.findComplaintsById(id);
        if(complaints==null) {
            throw new ApiException("Complaint not found");}
        complaintsRepository.delete(complaints);
    }


    //14
    public List<Complaints> getComplaintsByDateRange(LocalDate from, LocalDate to) {
        return complaintsRepository.findComplaintsByCreateAtDateBetween(from, to);
    }
}
