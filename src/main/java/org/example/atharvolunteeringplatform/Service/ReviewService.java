package org.example.atharvolunteeringplatform.Service;

import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Api.ApiException;
import org.example.atharvolunteeringplatform.Model.*;
import org.example.atharvolunteeringplatform.Repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OpportunityRepository opportunityRepository;
    private final StudentRepository studentRepository;
    private final StudentOpportunityRequestRepository studentOpportunityRequestRepository;
    private final MyUserRepository myUserRepository;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    //Integer supervisorId
    public void addReview(Integer opportunityId, Integer studentId, Review review, Integer supervisorId) {
        Opportunity opportunity = opportunityRepository.findOpportunityById(opportunityId);
        if (opportunity == null) {
            throw new ApiException("Opportunity not found");
        }

        Student student = studentRepository.findStudentById(studentId);
        if (student == null) {
            throw new ApiException("student not found");
        }

        // تحقق من وجود علاقة بين الطالب والفرصة وأنها مكتملة
        StudentOpportunityRequest request = studentOpportunityRequestRepository.findByStudentAndOpportunity(student, opportunity);
        if (request == null) {
            throw new ApiException("No application found for this student and opportunity");
        }
        if (!request.getStatus().equalsIgnoreCase("complete")) {
            throw new ApiException("The student has not completed the opportunity");
        }

        // تحقق من أن المستخدم هو مشرف المدرسة
        MyUser supervisor = myUserRepository.findMyUserById(supervisorId);
        if (supervisor == null) {
            throw new ApiException("Supervisor not found");
        }

        if (!supervisor.getRole().equalsIgnoreCase("supervisor")) {
            throw new ApiException("Only school supervisors can add reviews");
        }
        review.setCreatedAt(LocalDateTime.now());
        review.setSchool(student.getSchool());
        review.setOpportunity(opportunity);
        reviewRepository.save(review);
    }


}
//    public void updateReview(Integer reviewId, Review review) {
//        Review oldReview = reviewRepository.findReviewById(reviewId);
//        if (oldReview == null) {
//            throw new ApiException("Review not found");
//        }
//        oldReview.setRating(review.getRating());
//        oldReview.setCreatedAt(LocalDateTime.now());
//        reviewRepository.save(oldReview);
//    }

//    public void deleteReview(Integer reviewId) {
//        Review oldReview = reviewRepository.findReviewById(reviewId);
//        if (oldReview == null) {
//            throw new ApiException("Review not found");
//        }
//        reviewRepository.delete(oldReview);
//        }


