package org.example.atharvolunteeringplatform.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Model.Complaint;
import org.example.atharvolunteeringplatform.Model.MyUser;
import org.example.atharvolunteeringplatform.Service.ComplaintsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/Complaints")
@RequiredArgsConstructor

public class ComplaintsController {

    private final ComplaintsService complaintsService;

    @GetMapping("/all")
    public ResponseEntity<List<Complaint>> getAllComplaints() {
        return ResponseEntity.ok(complaintsService.getAllComplaints());
    }

    @PostMapping("/add")
    public ResponseEntity<String> addComplaint(@RequestBody @Valid Complaint complaint) {
        complaintsService.addComplaint(complaint);
        return ResponseEntity.ok("Complaint added successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateComplaint(@PathVariable Integer id, @RequestBody @Valid Complaint complaint) {
        complaintsService.updateComplaint(id, complaint);
        return ResponseEntity.ok("Complaint updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteComplaint(@PathVariable Integer id) {
        complaintsService.deleteComplaint(id);
        return ResponseEntity.ok("Complaint deleted successfully");
    }


    @GetMapping("/by-date/{from}/{to}")
    public ResponseEntity<?> getComplaintsByDate(@PathVariable LocalDateTime from, @PathVariable LocalDateTime  to) {
        List<Complaint> complaints = complaintsService.getComplaintsByDateRange(from, to);
        return ResponseEntity.status(HttpStatus.OK).body(complaints);
    }


    @GetMapping("/my-complaints/{status}")
    public ResponseEntity getMyComplaintsByStatus(/*@AuthenticationPrincipal*/ MyUser user, @PathVariable String status) {
        return ResponseEntity.status(HttpStatus.OK).body(
                complaintsService.getMyComplaintsByStatus(user.getId(), status)
        );
    }


}
