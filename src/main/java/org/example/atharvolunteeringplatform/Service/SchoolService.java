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
import org.example.atharvolunteeringplatform.Repository.MyUserRepository;
import org.example.atharvolunteeringplatform.Repository.SchoolRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final MyUserRepository myUserRepository;

    public List<School> getAllSchool() {
        return schoolRepository.findAll();
    }


    public void addSchool(SchoolDTO schoolDTO) {

        MyUser myUser = new MyUser();
        myUser.setEmail(schoolDTO.getEmail());
        myUser.setPhone_number(schoolDTO.getPhoneNumber());
        myUser.setPassword(schoolDTO.getPassword());



        myUser.setName(schoolDTO.getName());
        myUser.setRole("school");
        myUser.setCreated_at(LocalDateTime.now());


        myUserRepository.save(myUser);


        School school = new School();
        school.setCity(schoolDTO.getCity());
        school.setRegion(schoolDTO.getRegion());
        school.setSupervisorName(schoolDTO.getSupervisorName());
         school.setGender(schoolDTO.getGender());

        schoolRepository.save(school);
    }

    public void updateSchool(Integer schoolId, SchoolDTO schoolDTO ) {
        MyUser oldUser = myUserRepository.findMyUserById(schoolId);
        if (oldUser == null) {
            throw new ApiException("School not found");
        }

        School school = schoolRepository.findSchoolById(oldUser.getId());
        if (school == null) {
            throw new ApiException("School entity not found");
        }

        oldUser.setName(schoolDTO.getName());
        oldUser.setEmail(schoolDTO.getEmail());
        oldUser.setPhone_number(schoolDTO.getPhoneNumber());
        oldUser.setPassword(schoolDTO.getPassword());
        oldUser.setRole("school");


        myUserRepository.save(oldUser);


        school.setRegion(schoolDTO.getRegion());
        school.setSupervisorName(schoolDTO.getSupervisorName());
        school.setGender(schoolDTO.getGender());
        school.setCity(schoolDTO.getCity());

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


}
