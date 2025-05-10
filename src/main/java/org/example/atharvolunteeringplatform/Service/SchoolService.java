package org.example.atharvolunteeringplatform.Service;
//
//import com.example.final_project.API.ApiException;
//import com.example.final_project.DTO.SchoolDTO;
//import com.example.final_project.Model.MyUser;
//import com.example.final_project.Model.School;
//import com.example.final_project.Repository.MyUserRepository;
//import com.example.final_project.Repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Api.ApiException;
import org.example.atharvolunteeringplatform.DTO.SchoolDTO;
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
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final MyUserRepository myUserRepository;
    private final StudentRepository studentRepository;

    public List<School> getAllSchool() {
        return schoolRepository.findAll();
    }


    public void addSchool(SchoolDTO schoolDTO) {

        MyUser myUser = new MyUser();
        myUser.setName(schoolDTO.getName());
        myUser.setEmail(schoolDTO.getEmail());
        myUser.setPhone_number(schoolDTO.getPhoneNumber());
        myUser.setPassword(schoolDTO.getPassword());



        myUser.setRole("supervisor");
        myUser.setCreated_at(LocalDateTime.now());
        myUserRepository.save(myUser);


        School school = new School();
        school.setCity(schoolDTO.getCity());
        school.setRegion(schoolDTO.getRegion());
        school.setSupervisorName(schoolDTO.getSupervisorName());
         school.setGender(schoolDTO.getGender());
         school.setStatus("Pending");

        schoolRepository.save(school);
    }

    public void updateSchool(Integer schoolId, SchoolDTO schoolDTO ) {
        MyUser oldUser = myUserRepository.findMyUserById(schoolId);
        if (oldUser == null) {
            throw new ApiException("School not found");
        }

        // التحقق من أن حالة المدرسة مفعلة
        if (!"Active".equalsIgnoreCase(schoolDTO.getStatus())) {
            throw new ApiException("School must be in 'Active' status to allow update");
        }


        School school = schoolRepository.findSchoolById(oldUser.getId());
        if (school == null) {
            throw new ApiException("School entity not found");
        }

        oldUser.setName(schoolDTO.getName());
        oldUser.setEmail(schoolDTO.getEmail());
        oldUser.setPhone_number(schoolDTO.getPhoneNumber());
        oldUser.setPassword(schoolDTO.getPassword());
        oldUser.setRole("supervisor");


        myUserRepository.save(oldUser);


        school.setRegion(schoolDTO.getRegion());
        school.setSupervisorName(schoolDTO.getSupervisorName());
        school.setGender(schoolDTO.getGender());
        school.setCity(schoolDTO.getCity());
        school.setStatus("Pending");//الى ان تحصل على الموافقه بالتعديل

        schoolRepository.save(school);
    }

    public void deleteSchool(Integer schoolId) {
        MyUser oldUser = myUserRepository.findMyUserById(schoolId);
        if (oldUser == null) {
            throw new ApiException("school not found");
        }

        School school = schoolRepository.findSchoolById(oldUser.getId());
        if (school != null) {
            schoolRepository.delete(school);
        }

        myUserRepository.delete(oldUser);
    }

    //38
        public List<Student> getVolunteeringStudentsByGrade(String grade) {
            return studentRepository.findVolunteeringStudentsByGrade(grade);
        }


}
