package org.example.atharvolunteeringplatform.Service;

import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Api.ApiException;
import org.example.atharvolunteeringplatform.DTO.OpportunityDTO;
import org.example.atharvolunteeringplatform.Model.Opportunity;
import org.example.atharvolunteeringplatform.Model.Student;
import org.example.atharvolunteeringplatform.Model.StudentOpportunityRequest;
import org.example.atharvolunteeringplatform.Repository.OpportunityRepository;
import org.example.atharvolunteeringplatform.Repository.StudentOpportunityRequestRepository;
import org.example.atharvolunteeringplatform.Repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentOpportunityRequestService {


    private final StudentRepository studentRepository;
    private final StudentOpportunityRequestRepository studentOpportunityRequestRepository;
    private final OpportunityRepository opportunityrepository;



    public List<StudentOpportunityRequest> getAllRequests(){
        return studentOpportunityRequestRepository.findAll();
    }


    public void RequestOpportunity(Integer studentId, Integer opportunityId, StudentOpportunityRequest studentOpportunityRequest) {
        Student student = studentRepository.findStudentById(studentId);
        if (student == null) {
            throw new ApiException("Student not found");
        }

        Opportunity opportunity = opportunityrepository.findOpportunityById(opportunityId);
        if (opportunity == null) {
            throw new ApiException("Opportunity not found");
        }

        if (student.getStatus().equalsIgnoreCase("Inactive")) {
            throw new ApiException("Student account not active");
        }

        if (!student.getGender().equalsIgnoreCase(opportunity.getGender())) {
            throw new ApiException("Student not allowed to apply to opportunity due to gender");
        }


        boolean alreadyRequested = studentOpportunityRequestRepository.existsByStudentAndOpportunity(student, opportunity);
        if (alreadyRequested) {
            throw new ApiException("Student has already applied for this opportunity");
        }

        studentOpportunityRequest.setSupervisor_status("pending");
        studentOpportunityRequest.setOrganization_status("pending");
        studentOpportunityRequest.setStatus("pending");
        studentOpportunityRequest.setStudent(student);
        studentOpportunityRequest.setOpportunity(opportunity);
        studentOpportunityRequest.setApplied_at(LocalDateTime.now());

        studentOpportunityRequestRepository.save(studentOpportunityRequest);
    }



    public void deleteOpportunityRequest(Integer requestId) {

        StudentOpportunityRequest existingRequest = studentOpportunityRequestRepository.findStudentOpportunityRequestById(requestId);
        if (existingRequest == null) {
            throw new ApiException("Request not found");
        }


        studentOpportunityRequestRepository.delete(existingRequest);
    }

    //10
    public List<OpportunityDTO> getOpportunitiesByRequestStatus(Integer studentId, String status) {

        //نتحقق من ان الطالب مقدم على فرصه
        boolean hasRequests = studentOpportunityRequestRepository.existsByStudentId(studentId);
        if (!hasRequests) {
            throw new ApiException("There are no volunteer opportunities offered by the student.");
        }

        // استخرج الطلبات حسب الحالة
        List<StudentOpportunityRequest> requests = studentOpportunityRequestRepository.findByStudentIdAndStatus(studentId, status);

        List<OpportunityDTO> dtoList = new ArrayList<>();
        for (StudentOpportunityRequest request : requests) {
            dtoList.add(new OpportunityDTO(request.getOpportunity()));
        }
        return dtoList;
    }

    //31
    public void approveRequestByOrganization(Integer requestId, Integer organizationId) {
        StudentOpportunityRequest request = studentOpportunityRequestRepository.findStudentOpportunityRequestById(requestId);
        if(request==null){
            throw  new ApiException("Request not found");
        }
        Opportunity opportunity = request.getOpportunity();

//التحقق من ان الجهه هي نفس الجهه التي قدم عليها الطالب
        if (!opportunity.getOrganization().getId().equals(organizationId)) {
            throw new ApiException("You are not allowed to approve this request");
        }

        request.setOrganization_status("approved");
        studentOpportunityRequestRepository.save(request);
    }



    //15
    public List<StudentOpportunityRequest> getCompletedOpportunitiesByStudent(Integer studentId) {
        Student student = studentRepository.findStudentById(studentId);
        if (student == null) {
            throw new ApiException("Student not found");
        }

        return studentOpportunityRequestRepository.findCompletedOpportunitiesByStudentId(studentId);
    }

 
}
