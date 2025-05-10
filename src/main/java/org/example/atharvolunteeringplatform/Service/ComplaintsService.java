package org.example.atharvolunteeringplatform.Service;
import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Api.ApiException;
import org.example.atharvolunteeringplatform.Model.Complaint;
import org.example.atharvolunteeringplatform.Model.Student;
import org.example.atharvolunteeringplatform.Model.StudentOpportunityRequest;
import org.example.atharvolunteeringplatform.Repository.ComplaintsRepository;
import org.example.atharvolunteeringplatform.Repository.StudentOpportunityRequestRepository;
import org.example.atharvolunteeringplatform.Repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintsService {


    private final ComplaintsRepository complaintsRepository;
    private final StudentOpportunityRequestRepository studentOpportunityRequestRepository;
    private final StudentRepository studentRepository;

    public List<Complaint> getAllComplaints() {
        return complaintsRepository.findAll();
    }

    public void addComplaint(Complaint complaint) {

        // التحقق من ان الطالب مرفق في الشكوى
        Student student = complaint.getStudents();
        if (student == null || student.getId() == null) {
            throw new ApiException("Student is required to submit a complaint.");
        }

        Student existingStudent = studentRepository.findStudentById(student.getId());
        if (existingStudent == null) {
            throw new ApiException("Student not found.");
        }

        // التحقق من ان الطالب مسجل في فرصة تطوعية واحدة على الاقلل
        List<StudentOpportunityRequest> requests = studentOpportunityRequestRepository
                .findAllByStudentId(existingStudent.getId());

        if (requests.isEmpty()) {
            throw new ApiException("Student must be registered in a volunteering opportunity to submit a complaint.");
        }

        complaint.setCreateAt(LocalDateTime.now());
        complaint.setStatus("In Progress");

        complaintsRepository.save(complaint);

    }

    public void updateComplaint(Integer id, Complaint updatedComplaint) {
        Complaint complaint =complaintsRepository.findComplaintsById(id);
        if(complaint ==null) {
            throw new ApiException("Complaint not found");}

        complaint.setTitle(updatedComplaint.getTitle());
        complaint.setCreateAt(LocalDateTime.now());
        complaint.setDescription(updatedComplaint.getDescription());
        complaint.setStatus("In Progress");
        complaintsRepository.save(complaint);
    }

    public void deleteComplaint(Integer id) {
        Complaint complaint =complaintsRepository.findComplaintsById(id);
        if(complaint ==null) {
            throw new ApiException("Complaint not found");}
        complaintsRepository.delete(complaint);
    }


    //14
//    public List<Complaint> getComplaintsByDateRange(LocalDate from, LocalDate to) {
//        return complaintsRepository.findComplaintsByCreateAtDateBetween(from, to);
//    }

    //12
        public List<Complaint> getMyComplaints(Integer studentId) {
            return complaintsRepository.findByStudentsId(studentId);
        }




    }


