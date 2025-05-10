package org.example.atharvolunteeringplatform.Service;

import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Api.ApiException;
import org.example.atharvolunteeringplatform.DTO.SchoolDTO;
import org.example.atharvolunteeringplatform.Model.MyUser;
import org.example.atharvolunteeringplatform.Model.School;
import org.example.atharvolunteeringplatform.Model.Student;
import org.example.atharvolunteeringplatform.Model.StudentOpportunityRequest;
import org.example.atharvolunteeringplatform.Repository.MyUserRepository;
import org.example.atharvolunteeringplatform.Repository.SchoolRepository;
import org.example.atharvolunteeringplatform.Repository.StudentOpportunityRequestRepository;
 
import org.example.atharvolunteeringplatform.Repository.StudentRepository;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final MyUserRepository myUserRepository;

    private final StudentRepository studentRepository;
    private final StudentOpportunityRequestRepository studentOpportunityRequestRepository;
    private final JavaMailSender mailSender;


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
        school.setName(schoolDTO.getName());
        school.setCity(schoolDTO.getCity());
        school.setRegion(schoolDTO.getRegion());
        school.setSupervisorName(schoolDTO.getSupervisorName());
         school.setGender(schoolDTO.getGender());
         school.setStatus("Pending");
         school.setMyUser(myUser);

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

    public List<StudentOpportunityRequest> getAllRequestsForStudent(Integer studentId, Integer schoolId) {
        Student student = studentRepository.findStudentById(studentId);
        if (student == null) {
            throw new ApiException("Student not found");
        }

        if (!student.getSchool().getId().equals(schoolId)) {
            throw new ApiException("Unauthorized access");
        }

        return studentOpportunityRequestRepository.findAllByStudent_Id(studentId);
    }



    //42
    public List<Student> getNonVolunteersByGradeForSchool(String gradeLevel, Integer schoolId) {
        School school = schoolRepository.findSchoolById(schoolId);
        if (school == null) {
            throw new ApiException("school not found");
        }
        return studentRepository.findNonVolunteersStudents(gradeLevel, schoolId);
    }





    //40
    public void updateRequestStatus(Integer userId, Integer requestId, String status) {
        School school = schoolRepository.findSchoolById(userId);
        if (school == null) {
            throw new ApiException("School not found");
        }

        StudentOpportunityRequest request = studentOpportunityRequestRepository.findStudentOpportunityRequestById(requestId);
        if (request == null) {
            throw new ApiException("Request not found");
        }


        Student student = request.getStudent();
        if (!student.getSchool().getId().equals(school.getId())) {
            throw new ApiException("This student does not belong to your school");
        }


        if (request.getOpportunity().getEndDate().isAfter(LocalDate.now())) {
            throw new ApiException("Cannot update status. Opportunity has not ended yet");
        }


        if (!status.equals("completed") && !status.equals("incomplete")) {
            throw new ApiException("Invalid status. Must be 'completed' or 'incomplete'");
        }

        request.setStatus(status.toLowerCase());
        studentOpportunityRequestRepository.save(request);
    }

    //47
    public List<StudentOpportunityRequest> getStudentRequestsBySchoolUser(Integer userId) {

        School school = schoolRepository.findSchoolById(userId);

        if (school == null) {
            throw new ApiException("School not found");
        }


        return studentOpportunityRequestRepository.findAllBySchoolId(school.getId());
    }


    //53

    public void sendVolunteerDecisionEmail(String to, String status, String opportunityTitle, String organizationName, String location, LocalDate startDate, LocalDate endDate) {
        String subject;
        String body;

        if (status.equalsIgnoreCase("accepted")) {
            subject = "تم قبول طلب التطوع الخاص بك";
            body = "نود إبلاغك بأنه تم قبول طلبك للتطوع في الفرصة التالية:\n\n" +
                    "عنوان الفرصة: " + opportunityTitle + "\n" +
                    "الجهة المقدمة: " + organizationName + "\n" +
                    "الموقع: " + location + "\n" +
                    "تاريخ البداية: " + startDate + "\n" +
                    "تاريخ الانتهاء: " + endDate + "\n\n" +
                    "نتمنى لك تجربة تطوعية مثرية ومفيدة.\n\n" +
                    "مع تحيات فريق أثر.";
        } else {
            subject = "تم رفض طلب التطوع الخاص بك";
            body = "نأسف لإبلاغك بأنه لم يتم قبول طلبك للتطوع في الفرصة: " + opportunityTitle + ".\n" +
                    "نتمنى لك التوفيق في فرص أخرى قادمة.\n\n" +
                    "مع تحيات فريق أثر.";
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    //48
    public void updateOpportunityRequestStatus(Integer userId, Integer requestId, String status) {

        School school = schoolRepository.findSchoolById(userId);
        if (school == null) throw new ApiException("School not found");

        StudentOpportunityRequest request = studentOpportunityRequestRepository.findStudentOpportunityRequestById(requestId);
        if (request == null) throw new ApiException("Request not found");

        Student student = request.getStudent();
        if (student == null || !student.getSchool().getId().equals(school.getId())) {
            throw new ApiException("This student does not belong to your school");
        }

        if (request.getOpportunity().getEndDate().isBefore(LocalDate.now())) {
            throw new ApiException("Cannot update status. The opportunity has already ended");
        }

        if (!status.equalsIgnoreCase("accepted") && !status.equalsIgnoreCase("rejected")) {
            throw new ApiException("Invalid status. Must be 'accepted' or 'rejected'");
        }

        request.setSupervisor_status(status.toLowerCase());
        studentOpportunityRequestRepository.save(request);


        String email = student.getUserStudent().getEmail();
        sendVolunteerDecisionEmail(
                email,
                status,
                request.getOpportunity().getTitle(),
                request.getOpportunity().getOrganization().getName(),
                request.getOpportunity().getLocation(),
                request.getOpportunity().getStartDate(),
                request.getOpportunity().getEndDate()
        );
    }




}
