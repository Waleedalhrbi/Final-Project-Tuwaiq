package org.example.atharvolunteeringplatform.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Api.ApiResponse;
import org.example.atharvolunteeringplatform.DTO.OrganizationDTO;
import org.example.atharvolunteeringplatform.Model.MyUser;
import org.example.atharvolunteeringplatform.Model.StudentOpportunityRequest;
import org.example.atharvolunteeringplatform.Service.OrganizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping("/get-all")
    public ResponseEntity getAllOrganizations() {
        return ResponseEntity.status(HttpStatus.OK).body(organizationService.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity addOrganization(@RequestBody @Valid OrganizationDTO organizationDTO) {
        organizationService.createOrganization(organizationDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Organization added successfully"));
    }

    @PutMapping("/update")
    public ResponseEntity updateOrganization(/*@AuthenticationPrincipal*/ MyUser myUser, @RequestBody @Valid OrganizationDTO organizationDTO) {
        organizationService.updateOrganization(myUser.getId(), organizationDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Organization updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteOrganization( @PathVariable Integer id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Organization deleted successfully"));
    }

    @GetMapping("/count/volunteers/{organizationId}")
    public ResponseEntity getVolunteersCount(@PathVariable Integer organizationId) {
        int count = organizationService.volunteersCount(organizationId);
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

    @GetMapping("/count/opportunities/{organizationId}")
    public ResponseEntity getOpportunitiesCount(@PathVariable Integer organizationId) {
        int count = organizationService.opportunitiesCount(organizationId);
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

    @GetMapping("/total-hours/{organizationId}")
    public ResponseEntity getTotalVolunteeringHours(@PathVariable Integer organizationId) {
        int totalHours = organizationService.getTotalVolunteeringHours(organizationId);
        return ResponseEntity.status(HttpStatus.OK).body(totalHours);
    }


    @GetMapping("/organization/pending/{organizationId}")
    public ResponseEntity<?> getPendingRequests(@PathVariable Integer organizationId) {
        List<StudentOpportunityRequest> requests = organizationService.getPendingRequestsByOrganization(organizationId);
        return ResponseEntity.status(HttpStatus.OK).body(requests);
    }

    @PostMapping("/reject-request/{requestId}/{organizationId}")
    public ResponseEntity<?> rejectVolunteerRequest(@PathVariable Integer requestId,@PathVariable Integer organizationId ) {
        organizationService.rejectVolunteerRequest(requestId,organizationId);
        return ResponseEntity.status(HttpStatus.OK).body("Volunteer request rejected and email sent.");
    }

    @PostMapping("/accept-request/{requestId}/{organizationId}")
    public ResponseEntity<?> acceptVolunteerRequest(@PathVariable Integer requestId, @PathVariable Integer organizationId) {
        organizationService.acceptVolunteerRequest(organizationId, requestId);
        return ResponseEntity.status(HttpStatus.OK).body("Volunteer request accepted and email sent.");
    }

    @GetMapping("/organization/history/{organizationId}")
    public ResponseEntity<?> getVolunteerRequestHistory(@PathVariable Integer organizationId) {
        List<StudentOpportunityRequest> history = organizationService.getVolunteerRequestHistory(organizationId);
        return ResponseEntity.status(HttpStatus.OK).body(history);
    }

    @PutMapping("/open/{opportunityId}/{organizationId}")
    public ResponseEntity<?> openOpportunity(@PathVariable Integer opportunityId, @PathVariable Integer organizationId) {
        organizationService.openOpportunity(opportunityId, organizationId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Opportunity status updated to open"));
    }

    @PutMapping("/close/{opportunityId}/{organizationId}")
    public ResponseEntity<?> closeOpportunity(@PathVariable Integer opportunityId, @PathVariable Integer organizationId) {
        organizationService.closeOpportunity(opportunityId, organizationId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Opportunity status updated to closed"));
    }

    @PutMapping("/activate-organization/{organizationId}")
    public ResponseEntity activateOrganization(@PathVariable Integer organizationId) {
        organizationService.activateOrganization(organizationId);
        return ResponseEntity.ok(new ApiResponse("Organization has been activated successfully"));
    }


}
