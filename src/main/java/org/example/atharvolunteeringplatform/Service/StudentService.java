package org.example.atharvolunteeringplatform.Service;

import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Api.ApiException;
import org.example.atharvolunteeringplatform.DTO.StudentDTO;
import org.example.atharvolunteeringplatform.Model.MyUser;
import org.example.atharvolunteeringplatform.Model.Opportunity;
import org.example.atharvolunteeringplatform.Model.Student;
import org.example.atharvolunteeringplatform.Model.StudentOpportunityRequest;
import org.example.atharvolunteeringplatform.Repository.MyUserRepository;
import org.example.atharvolunteeringplatform.Repository.OpportunityRepository;
import org.example.atharvolunteeringplatform.Repository.StudentOpportunityRequestRepository;
import org.example.atharvolunteeringplatform.Repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentService {


    private final StudentRepository studentRepository;
    private final MyUserRepository myUserRepository;
    private final OpportunityRepository opportunityRepository;
    private final StudentOpportunityRequestRepository studentOpportunityRequestRepository;


    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void addStudent(StudentDTO studentDTO) {

        MyUser myUser = new MyUser();
        myUser.setName(studentDTO.getName());
        myUser.setEmail(studentDTO.getEmail());
        myUser.setPhone_number(studentDTO.getPhone_number());

        myUser.setPassword(studentDTO.getPassword());
        myUser.setRole("student");
        myUser.setCreated_at(LocalDateTime.now());


        myUserRepository.save(myUser);


        Student student = new Student();
        student.setSchool_name(studentDTO.getSchool_name());
        student.setAge(studentDTO.getAge());
        student.setGrade_level(studentDTO.getGrade_level());
        student.setGender(studentDTO.getGender());
        student.setStatus("Inactive");
        student.setUserStudent(myUser);


        studentRepository.save(student);
    }

    public void updateStudent(Integer studentId, StudentDTO studentDTO) {
        MyUser oldUser = myUserRepository.findMyUserById(studentId);
        if (oldUser == null) {
            throw new ApiException("Student not found");
        }

        Student student = studentRepository.findStudentById(oldUser.getId());
        if (student == null) {
            throw new ApiException("Student entity not found");
        }


        oldUser.setName(studentDTO.getName());
        oldUser.setEmail(studentDTO.getEmail());
        oldUser.setPhone_number(studentDTO.getPhone_number());
        oldUser.setPassword(studentDTO.getPassword());
        oldUser.setRole("student");


        myUserRepository.save(oldUser);


        student.setSchool_name(studentDTO.getSchool_name());
        student.setAge(studentDTO.getAge());
        student.setGrade_level(studentDTO.getGrade_level());
        student.setGender(studentDTO.getGender());
        student.setStatus(studentDTO.getStatus());
        student.setTotal_hours(studentDTO.getTotal_hours());
        student.setBadges_count(studentDTO.getBadges_count());

        studentRepository.save(student);
    }

    public void deleteStudent(String email) {
        MyUser oldUser = myUserRepository.findMyUserByEmail(email);
        if (oldUser == null) {
            throw new ApiException("Student not found");
        }

        Student student = studentRepository.findStudentById(oldUser.getId());
        if (student != null) {
            studentRepository.delete(student);
        }

        myUserRepository.delete(oldUser);
    }

    //3
    public List<Opportunity> getOpportunitiesSortedByHours() {
        return opportunityRepository.findAllByOrderByHoursDesc();
    }

    //7
    public List<Opportunity> getOpportunitiesByDateRange(LocalDate from, LocalDate to) {
        return opportunityRepository.findByStartDateBetween(from, to);
    }

    //9
    public List<StudentOpportunityRequest> getMyRequests(Integer studentId) {
        Student student = studentRepository.findStudentById(studentId);
        if (student == null) {
            throw new ApiException("Student not found");
        }
        return studentOpportunityRequestRepository.findAllByStudent(student);
    }


    //16
    public Map<String, Integer> getStudentHoursSummary(Integer studentId) {
        Student student = studentRepository.findStudentById(studentId);
        if (student == null) {
            throw new ApiException("Student not found");
        }

        int completedHours = 0;
        if (student.getTotal_hours() != null) {
            completedHours = student.getTotal_hours();
        }
        int requiredHours = 40;
        int remainingHours = Math.max(requiredHours - completedHours, 0);

        Map<String, Integer> hoursSummary = new HashMap<>();
        hoursSummary.put("completed_hours", completedHours);
        hoursSummary.put("remaining_hours", remainingHours);

        return hoursSummary;
    }


}