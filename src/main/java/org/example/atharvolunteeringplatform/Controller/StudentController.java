package org.example.atharvolunteeringplatform.Controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Api.ApiResponse;
import org.example.atharvolunteeringplatform.DTO.StudentDTO;
import org.example.atharvolunteeringplatform.Model.MyUser;
import org.example.atharvolunteeringplatform.Service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/get-all")
    public ResponseEntity getAllStudents() {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getAllStudents());
    }

    @PostMapping("/add")
    public ResponseEntity addStudent(@RequestBody @Valid StudentDTO studentDTO) {
        studentService.addStudent(studentDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Student added successfully"));
    }

    @PutMapping("/update")
    public ResponseEntity updateStudent(/*@AuthenticationPrincipal*/ MyUser myUser, @RequestBody @Valid StudentDTO studentDTO) {
        studentService.updateStudent(myUser.getId(), studentDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Student updated successfully"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteStudent(/*@AuthenticationPrincipal*/ MyUser myUser) {
        studentService.deleteStudent(myUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Student deleted successfully"));
    }
}
