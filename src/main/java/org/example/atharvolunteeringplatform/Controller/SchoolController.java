package org.example.atharvolunteeringplatform.Controller;

//import com.example.final_project.DTO.SchoolDTO;
//import com.example.final_project.Model.School;
//import com.example.final_project.Service.SchoolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.DTO.SchoolDTO;
import org.example.atharvolunteeringplatform.Model.School;
import org.example.atharvolunteeringplatform.Service.SchoolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
