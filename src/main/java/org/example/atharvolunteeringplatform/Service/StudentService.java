package org.example.atharvolunteeringplatform.Service;

import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Api.ApiException;
import org.example.atharvolunteeringplatform.DTO.StudentDTO;
import org.example.atharvolunteeringplatform.Model.MyUser;
import org.example.atharvolunteeringplatform.Model.School;
import org.example.atharvolunteeringplatform.Model.Student;
import org.example.atharvolunteeringplatform.Repository.MyUserRepository;
import org.example.atharvolunteeringplatform.Repository.SchoolRepository;
import org.example.atharvolunteeringplatform.Repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {


    private final StudentRepository studentRepository;
    private final MyUserRepository myUserRepository;
    private final SchoolRepository schoolRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void addStudent(StudentDTO studentDTO) {

        List<School> schools = schoolRepository.findAll();
        MyUser myUser = new MyUser();
        myUser.setName(studentDTO.getName());
        myUser.setEmail(studentDTO.getEmail());
        myUser.setPhone_number(studentDTO.getPhone_number());

        myUser.setPassword(studentDTO.getPassword());
        myUser.setRole("student");
        myUser.setCreated_at(LocalDateTime.now());


        myUserRepository.save(myUser);


        Student student = new Student();
        student.setName(studentDTO.getName());
        student.setSchool_name(studentDTO.getSchool_name());
        student.setAge(studentDTO.getAge());
        student.setGrade_level(studentDTO.getGrade_level());
        student.setGender(studentDTO.getGender());
        student.setStatus("Inactive");

        School matchedSchool = null;
        for (School school : schools) {
            if (school != null && school.getName() != null && studentDTO.getSchool_name().equalsIgnoreCase(school.getName())) {
                matchedSchool = school;
                break;
            }
        }

        if (matchedSchool == null) {
            throw new ApiException("School with name '" + studentDTO.getSchool_name() + "' not found");
        }

        student.setSchool(matchedSchool);

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
}