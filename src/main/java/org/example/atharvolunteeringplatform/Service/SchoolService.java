package org.example.atharvolunteeringplatform.Service;

import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Api.ApiException;
import org.example.atharvolunteeringplatform.DTO.SchoolDTO;
import org.example.atharvolunteeringplatform.Model.*;
import org.example.atharvolunteeringplatform.Repository.*;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final MyUserRepository myUserRepository;
    private final StudentRepository studentRepository;
    private final BadgeRepository badgeRepository;
    private final StudentOpportunityRequestRepository studentOpportunityRequestRepository;
    private final JavaMailSender mailSender;

    public List<School> getAllSchool() {
        return schoolRepository.findAll();
    }

    public void addSchool(SchoolDTO schoolDTO) {
        MyUser myUser = new MyUser();
        myUser.setName(schoolDTO.getName());
        myUser.setUsername(schoolDTO.getUsername());
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

    public void updateSchool(Integer schoolId, SchoolDTO schoolDTO) {
        MyUser oldUser = myUserRepository.findMyUserById(schoolId);
        if (oldUser == null) throw new ApiException("School not found");

        if (!"Active".equalsIgnoreCase(schoolDTO.getStatus())) {
            throw new ApiException("School must be in 'Active' status to allow update");
        }

        School school = schoolRepository.findSchoolById(oldUser.getId());
        if (school == null) throw new ApiException("School entity not found");

        oldUser.setName(schoolDTO.getName());
        oldUser.setUsername(schoolDTO.getUsername());
        oldUser.setEmail(schoolDTO.getEmail());
        oldUser.setPhone_number(schoolDTO.getPhoneNumber());
        oldUser.setPassword(schoolDTO.getPassword());
        oldUser.setRole("supervisor");
        myUserRepository.save(oldUser);

        school.setRegion(schoolDTO.getRegion());
        school.setSupervisorName(schoolDTO.getSupervisorName());
        school.setCity(schoolDTO.getCity());
        school.setStatus("Pending");

        schoolRepository.save(school);
    }

    public void deleteSchool(Integer schoolId) {
        MyUser oldUser = myUserRepository.findMyUserById(schoolId);
        if (oldUser == null) throw new ApiException("School not found");

        School school = schoolRepository.findSchoolById(oldUser.getId());
        if (school != null) schoolRepository.delete(school);

        myUserRepository.delete(oldUser);
    }

    public List<Student> getVolunteeringStudentsByGrade(String grade, Integer schoolID) {
        School school = schoolRepository.findSchoolById(schoolID);
        if (school == null) throw new ApiException("School not found");

        return studentRepository.findVolunteeringStudentsByGradeAndSchoolId(grade, schoolID);
    }

    public List<StudentOpportunityRequest> getAllRequestsForStudent(Integer studentId, Integer schoolId) {
        Student student = studentRepository.findStudentById(studentId);
        if (student == null) throw new ApiException("Student not found");

        if (!student.getSchool().getId().equals(schoolId)) {
            throw new ApiException("Unauthorized access");
        }

        return studentOpportunityRequestRepository.findAllByStudent_Id(studentId);
    }

    public List<Student> getNonVolunteersByGradeForSchool(String gradeLevel, Integer schoolId) {
        School school = schoolRepository.findSchoolById(schoolId);
        if (school == null) throw new ApiException("School not found");

        return studentRepository.findNonVolunteersStudents(gradeLevel, schoolId);
    }

    public void updateRequestStatus(Integer userId, Integer requestId, String status) {
        School school = schoolRepository.findSchoolById(userId);
        if (school == null) throw new ApiException("School not found");

        StudentOpportunityRequest request = studentOpportunityRequestRepository.findStudentOpportunityRequestById(requestId);
        if (request == null) throw new ApiException("Request not found");

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

        if (request.getStatus().equalsIgnoreCase(status)) {
            throw new ApiException("Request already " + status);
        }

        if (status.equalsIgnoreCase("completed") && !request.getStatus().equalsIgnoreCase("completed")) {
            Integer opportunityHours = request.getOpportunity().getHours();
            student.setTotal_hours(student.getTotal_hours() + opportunityHours);
        }

        request.setStatus(status.toLowerCase());
        studentOpportunityRequestRepository.save(request);
        studentRepository.save(student);

        assignBadgeIfEligible(student);
    }

    public void approveStudentAccount(Integer studentId, Integer schoolId) {
        Student student = studentRepository.findStudentById(studentId);
        if (student == null || (!student.getStatus().equalsIgnoreCase("Inactive") && !student.getStatus().equals("Pending"))) {
            throw new ApiException("Student not found or already approved/rejected");
        }

        if (!student.getSchool().getId().equals(schoolId)) {
            throw new ApiException("Unauthorized: You can only approve students from your own school");
        }

        student.setStatus("Active");
        studentRepository.save(student);
    }


    public void rejectStudentAccount(Integer studentId, Integer schoolId) {
        Student student = studentRepository.findStudentById(studentId);
        if (student == null) throw new ApiException("Student not found");

        if (!student.getSchool().getId().equals(schoolId)) {
            throw new ApiException("Unauthorized: You can only reject students from your own school");
        }

        if (!student.getStatus().equalsIgnoreCase("Pending") && !student.getStatus().equalsIgnoreCase("Inactive")) {
            throw new ApiException("The account is already rejected or active");
        }

        student.setStatus("Rejected");
        studentRepository.save(student);
    }

    public List<StudentOpportunityRequest> getStudentRequestsBySchoolUser(Integer userId) {
        School school = schoolRepository.findSchoolById(userId);
        if (school == null) throw new ApiException("School not found");

        return studentOpportunityRequestRepository.findAllBySchoolId(school.getId());
    }

    public void sendVolunteerDecisionEmail(String to, String status, String opportunityTitle, String organizationName, String location, LocalDate startDate, LocalDate endDate) {
        String subject;
        String body;

        if (status.equalsIgnoreCase("approved")) {
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

    public void acceptOrRejectRequest(Integer userId, Integer requestId, String status) {
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

        request.setSupervisor_status(status.toLowerCase());

        if (status.equalsIgnoreCase("rejected")) {
            request.setStatus("rejected");
        } else if (status.equalsIgnoreCase("approved") && "approved".equalsIgnoreCase(request.getOrganization_status())) {
            request.setStatus("approved");
            request.setApproved_at(LocalDateTime.now());
        }

        studentOpportunityRequestRepository.save(request);

        String email = student.getUserStudent().getEmail();
        sendVolunteerDecisionEmail(email, request.getStatus(),
                request.getOpportunity().getTitle(),
                request.getOpportunity().getOrganization().getName(),
                request.getOpportunity().getLocation(),
                request.getOpportunity().getStartDate(),
                request.getOpportunity().getEndDate());
    }

    public Map<String, Object> getStudentDetailsResponse(Integer studentId, MyUser currentUser) {

        if (!currentUser.getRole().equalsIgnoreCase("supervisor")) {
            throw new ApiException("Access denied: Only supervisors can view this information");
        }


        School supervisorSchool = schoolRepository.findSchoolById(currentUser.getId());
        if (supervisorSchool == null) {
            throw new ApiException("Supervisor's school not found");
        }


        Student student = studentRepository.findStudentById(studentId);
        if (student == null) {
            throw new ApiException("Student not found");
        }


        if (!student.getSchool().getId().equals(supervisorSchool.getId())) {
            throw new ApiException("Access denied: Student does not belong to your school");
        }


        Map<String, Object> response = new HashMap<>();
        response.put("id", student.getId());
        response.put("name", student.getName());
        response.put("school_name", student.getSchool_name());
        response.put("age", student.getAge());
        response.put("grade_level", student.getGrade_level());
        response.put("gender", student.getGender());
        response.put("status", student.getStatus());
        response.put("total_hours", student.getTotal_hours());
        response.put("badges_count", student.getBadges_count());

        return response;
    }

    public void activateSchool(Integer schoolId) {
        School school = schoolRepository.findSchoolById(schoolId);
        if (school == null) throw new ApiException("School not found");

        if ("Active".equalsIgnoreCase(school.getStatus())) {
            throw new ApiException("School is already active");
        }

        school.setStatus("Active");
        schoolRepository.save(school);
    }

    public void notifyNonVolunteeringStudent(Integer studentId, Integer schoolId) {
        Student student = studentRepository.findStudentById(studentId);
        if (student == null) {
            throw new ApiException("Student not found");
        }


        if (!student.getSchool().getId().equals(schoolId)) {
            throw new ApiException("Unauthorized: You can only notify students from your own school");
        }

        if (student.getTotal_hours() != 0) {
            throw new ApiException("Student already has volunteering hours");
        }

        boolean hasRequests = studentOpportunityRequestRepository.existsByStudentId(studentId);
        if (hasRequests) {
            throw new ApiException("Student has already applied for a volunteering opportunity");
        }

        String to = student.getUserStudent().getEmail();
        String subject = "تذكير بالتسجيل في الفرص التطوعية";
        String body = "عزيزي/عزيزتي " + student.getName() + "،\n\n" +
                "لاحظنا أنك لم تقم بالتسجيل في أي فرصة تطوعية حتى الآن.\n" +
                "نحثك على المسارعة بالتسجيل والمشاركة في الأعمال التطوعية المتاحة عبر منصة أثر.\n\n" +
                "المساهمة المجتمعية لها أثر كبير، ونحن بانتظار مشاركتك.\n\n" +
                "مع تحيات فريق أثر.";

        sendDecisionEmail(to, subject, body);
    }


    public void sendDecisionEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void assignBadgeIfEligible(Student student) {
        Integer totalHours = student.getTotal_hours();
        List<Badge> allBadges = badgeRepository.findAllByOrderByCriteriaAsc();
        Set<Badge> ownedBadges = student.getBadges();

        for (Badge badge : allBadges) {
            boolean alreadyOwned = ownedBadges.stream().anyMatch(b -> b.getId().equals(badge.getId()));
            if (totalHours >= badge.getCriteria() && !alreadyOwned) {
                student.getBadges().add(badge);
            }
        }

        studentRepository.save(student);
    }
}
