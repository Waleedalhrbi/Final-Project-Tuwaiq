package org.example.atharvolunteeringplatform.Controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Api.ApiResponse;
import org.example.atharvolunteeringplatform.DTO.OpportunityDTO;
import org.example.atharvolunteeringplatform.Model.StudentOpportunityRequest;
import org.example.atharvolunteeringplatform.Service.StudentOpportunityRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student-opportunity-request")
@RequiredArgsConstructor
public class StudentOpportunityRequestController {

    private final StudentOpportunityRequestService studentOpportunityRequestService;

    @GetMapping("/get-all")
    public ResponseEntity getAllRequests() {
        return ResponseEntity.status(HttpStatus.OK).body(studentOpportunityRequestService.getAllRequests());
    }

    @PostMapping("/request/{studentId}/{opportunityId}")
    public ResponseEntity requestOpportunity(@PathVariable Integer studentId, @PathVariable Integer opportunityId, @Valid @RequestBody StudentOpportunityRequest studentOpportunityRequest) {
        studentOpportunityRequestService.RequestOpportunity(studentId, opportunityId, studentOpportunityRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Opportunity request submitted successfully"));
    }

    @DeleteMapping("/delete/{requestId}")
    public ResponseEntity deleteOpportunityRequest(@PathVariable Integer requestId) {
        studentOpportunityRequestService.deleteOpportunityRequest(requestId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Opportunity request deleted successfully"));
    }

    //10
    @GetMapping("/student/opportunities/filter")
    public ResponseEntity getStudentOpportunitiesByStatus(/*@AuthenticationPrincipal Student Student*/@RequestParam Integer studentId, @RequestParam String status) {
        List<OpportunityDTO> opportunities = studentOpportunityRequestService.getOpportunitiesByRequestStatus(studentId, status);
        if(opportunities.isEmpty()) {
            return ResponseEntity.ok().body("There are no results for this data.");
        }
        return ResponseEntity.ok(opportunities);
    }

    //31
    @PutMapping("/approve/{requestId}/organization/{organizationId}")
    public ResponseEntity approveRequestByOrganization(@PathVariable Integer requestId, /*@AuthenticationPrincipal Student Student*/@PathVariable Integer organizationId) {

        studentOpportunityRequestService.approveRequestByOrganization(requestId, organizationId);
        return ResponseEntity.ok("Request approved successfully by organization");
    }




}