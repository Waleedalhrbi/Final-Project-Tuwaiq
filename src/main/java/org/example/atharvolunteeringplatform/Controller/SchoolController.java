package org.example.atharvolunteeringplatform.Controller;

//import com.example.final_project.DTO.SchoolDTO;
//import com.example.final_project.Model.School;
//import com.example.final_project.Service.SchoolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Api.ApiResponse;
import org.example.atharvolunteeringplatform.DTO.SchoolDTO;
import org.example.atharvolunteeringplatform.Model.MyUser;
import org.example.atharvolunteeringplatform.Model.School;
import org.example.atharvolunteeringplatform.Model.Student;
import org.example.atharvolunteeringplatform.Model.StudentOpportunityRequest;
import org.example.atharvolunteeringplatform.Service.OrganizationService;
import org.example.atharvolunteeringplatform.Service.SchoolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/school")
@RequiredArgsConstructor
public class SchoolController {


    private final SchoolService schoolService;

    @GetMapping("/all")
    public ResponseEntity<List<School>> getAllSchools() {
        List<School> schools = schoolService.getAllSchool();
        return ResponseEntity.ok(schools);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addSchool(@RequestBody @Valid SchoolDTO schoolDTO) {
        schoolService.addSchool(schoolDTO);
        return ResponseEntity.ok("School added successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateSchool(/*@AuthenticationPrincipal MyUser myUser*/@PathVariable Integer id, @RequestBody @Valid SchoolDTO schoolDTO) {
        schoolService.updateSchool(id, schoolDTO);
        return ResponseEntity.ok("School updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSchool(/*@AuthenticationPrincipal MyUser myUser*/@PathVariable Integer id) {
        schoolService.deleteSchool(id);
        return ResponseEntity.ok("School deleted successfully");
    }

    //38
    @GetMapping("/volunteers/{grade}")
    public ResponseEntity getVolunteeringStudents(@PathVariable String grade, /*@AuthenticationPrincipal*/ MyUser user) {
        List<Student> students = schoolService.getVolunteeringStudentsByGrade(grade, user.getId());
        return ResponseEntity.ok(students);
    }

    @GetMapping("/students/non-volunteers/{gradeLevel}/{schoolId}")
    public ResponseEntity<?> getNonVolunteers(/*@AuthenticationPrincipal MyUser myUser*/ @PathVariable String gradeLevel, @PathVariable Integer schoolId) {
        List<Student> students = schoolService.getNonVolunteersByGradeForSchool(gradeLevel, schoolId);
        return ResponseEntity.status(HttpStatus.OK).body(students);
    }

    @GetMapping("/student/{studentId}/school/{schoolId}/opportunities")
    public ResponseEntity<?> getAllRequestsForStudent(@PathVariable Integer studentId, @PathVariable Integer schoolId) {
        List<StudentOpportunityRequest> requests = schoolService.getAllRequestsForStudent(studentId, schoolId);
        return ResponseEntity.status(HttpStatus.OK).body(requests);
    }


 
    @PutMapping("/opportunity-request/{requestId}/status/{status}")
    public ResponseEntity updateRequestStatus(/*@AuthenticationPrincipal*/ MyUser user, @PathVariable Integer requestId, @PathVariable String status) {

        schoolService.updateRequestStatus(user.getId(), requestId, status);
        return ResponseEntity.ok("Request status updated to: " + status);
    }


    @GetMapping("/requests")
    public ResponseEntity<List<StudentOpportunityRequest>> getStudentRequestsBySchoolUser(/*@AuthenticationPrincipal*/ MyUser user) {

        List<StudentOpportunityRequest> studentRequests = schoolService.getStudentRequestsBySchoolUser(user.getId());

        return ResponseEntity.status(HttpStatus.OK).body(studentRequests);
    }

    @PutMapping("/accept/{requestId}")
    public ResponseEntity<String> acceptRequest(/*@AuthenticationPrincipal*/ MyUser user, @PathVariable Integer requestId) {
        schoolService.acceptOrRejectRequest(user.getId(), requestId, "accepted");
        return ResponseEntity.ok("Request accepted successfully");
    }

    @PutMapping("/reject/{requestId}")
    public ResponseEntity<String> rejectRequest(/*@AuthenticationPrincipal*/ MyUser user, @PathVariable Integer requestId) {
        schoolService.acceptOrRejectRequest(user.getId(), requestId, "rejected");
        return ResponseEntity.ok("Request rejected successfully");
    }



    @PutMapping("/students/{studentId}/active")
    public ResponseEntity<String> approveStudent(@PathVariable Integer studentId) {
        schoolService.approveStudentAccount(studentId);
        return ResponseEntity.ok("Student account Activated successfully");
    }


    //43
    @PostMapping("/notify-non-volunteering/{studentId}")
    public ResponseEntity<String> notifyNonVolunteeringStudent(@PathVariable Integer studentId) {
        schoolService.notifyNonVolunteeringStudent(studentId);
        return ResponseEntity.ok("Email send successfully");
    }


    //46
    @PutMapping("/students/reject/{studentId}")
    public ResponseEntity<String> rejectStudent(@PathVariable Integer studentId) {
        schoolService.rejectStudentAccount(studentId);
        return ResponseEntity.ok("Student account has been rejected.");
    }


    //50
    @GetMapping("/students/details/{studentId}")
    public ResponseEntity  getStudentDetails(@PathVariable Integer studentId) {
        Map<String, Object> response = schoolService.getStudentDetailsResponse(studentId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<String> activateSchool(@PathVariable Integer id) {
        schoolService.activateSchool(id);
        return ResponseEntity.ok("School account activated successfully");
    }


}
