package org.example.atharvolunteeringplatform.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Api.ApiResponse;
import org.example.atharvolunteeringplatform.Model.Opportunity;
import org.example.atharvolunteeringplatform.Repository.OpportunityRepository;
import org.example.atharvolunteeringplatform.Service.OpportunityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/opportunity")
@RequiredArgsConstructor
public class OpportunityController {

    private final OpportunityService opportunityService;
    private final OpportunityRepository opportunityRepository;

    @GetMapping("/get-all")
    public ResponseEntity getAllOpportunities() {
        return ResponseEntity.status(HttpStatus.OK).body(opportunityService.findAllOpportunities());
    }

    @PostMapping("/add/{organizationId}")
    public ResponseEntity addOpportunity(@RequestBody @Valid Opportunity opportunity,/*@AuthenticationPrincipal*/ @PathVariable Integer organizationId) {
        opportunityService.createOpportunity(opportunity,organizationId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Opportunity added successfully and pending approval"));
    }

    @PutMapping("/update/{opportunityId}/{organizationId}")
    public ResponseEntity updateOpportunity(/*@AuthenticationPrincipal*/ @PathVariable Integer opportunityId, @PathVariable Integer organizationId, @RequestBody @Valid Opportunity opportunity) {

        opportunityService.updateOpportunity(opportunityId, organizationId, opportunity);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Opportunity updated successfully. A decision email will be sent once the edit is reviewed."));
    }

    @DeleteMapping("/delete/{opportunityId}")
    public ResponseEntity deleteOpportunity(/*@AuthenticationPrincipal*/ @PathVariable Integer opportunityId) {
        opportunityService.deleteOpportunity(opportunityId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Opportunity deleted successfully"));
    }

    @GetMapping("/sorted-by-capacity/{organizationId}")
    public ResponseEntity getOpportunitiesSorted(@PathVariable Integer organizationId) {
        List<Opportunity> sorted = opportunityService.getOpportunitiesSortedByCapacity(organizationId);
        return ResponseEntity.status(HttpStatus.OK).body(sorted);
    }

    @GetMapping("/count/open/{organizationId}")
    public ResponseEntity getOpenCount(@PathVariable Integer organizationId) {
        return ResponseEntity.status(HttpStatus.OK).body(opportunityService.countByStatus(organizationId, "Open"));
    }

    @GetMapping("/count/pending/{organizationId}")
    public ResponseEntity getPendingCount(@PathVariable Integer organizationId) {
        return ResponseEntity.status(HttpStatus.OK).body(opportunityService.countByStatus(organizationId, "pending"));
    }

    @GetMapping("/count/rejected/{organizationId}")
    public ResponseEntity getRejectedCount(@PathVariable Integer organizationId) {
        return ResponseEntity.status(HttpStatus.OK).body(opportunityService.countByStatus(organizationId, "rejected"));
    }

    @GetMapping("/count/closed/{organizationId}")
    public ResponseEntity getClosedCount(@PathVariable Integer organizationId) {
        return ResponseEntity.status(HttpStatus.OK).body(opportunityService.countByStatus(organizationId, "closed"));
    }

    @GetMapping("/count/total/{organizationId}")
    public ResponseEntity getTotalCount(@PathVariable Integer organizationId) {
        return ResponseEntity.status(HttpStatus.OK).body(opportunityService.countTotalOpportunities(organizationId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getOpportunitiesByStatus(@PathVariable String status) {
        List<Opportunity> opportunities = opportunityService.getOpportunitiesByStatus(status);
        return ResponseEntity.status(HttpStatus.OK).body(opportunities);
    }

    @PostMapping("/accept/{id}")
    public ResponseEntity<?> acceptOpportunity(@PathVariable Integer id) {
        opportunityService.acceptOpportunity(id);
        return ResponseEntity.status(HttpStatus.OK).body("Opportunity accepted and email sent.");
    }

    @PostMapping("/accept-edit/{id}")
    public ResponseEntity<?> acceptOpportunityEdit(@PathVariable Integer id) {
        opportunityService.acceptOpportunityEdit(id);
        return ResponseEntity.status(HttpStatus.OK).body("Opportunity edit accepted and email sent.");
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<?> rejectOpportunity(@PathVariable Integer id) {
        opportunityService.rejectOpportunity(id);
        return ResponseEntity.status(HttpStatus.OK).body("Opportunity rejected and email sent.");
    }

    @PostMapping("/reject-edit/{id}")
    public ResponseEntity<?> rejectOpportunityEdit(@PathVariable Integer id) {
        opportunityService.rejectOpportunityEdit(id);
        return ResponseEntity.status(HttpStatus.OK).body("Opportunity edit rejected and email sent.");
    }
}



