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

        public void deleteReview(Integer reviewId) {
        Review oldReview = reviewRepository.findReviewById(reviewId);
        if (oldReview == null) {
            throw new ApiException("Review not found");
        }
        reviewRepository.delete(oldReview);
        }

    //41
    public String reviewOpportunity(Integer opportunityId, Integer schoolId, Review review) {
        List<StudentOpportunityRequest> requests = studentOpportunityRequestRepository.findCompletedRequestsForOpportunity(opportunityId, schoolId);

        if (requests.isEmpty()) {
            return "You cannot rate this opportunity because it is not associated with a student from the school or has not yet been completed.";
        }

        Opportunity opportunity = opportunityRepository.findOpportunityById(opportunityId);
        if (opportunity == null) {
        throw new ApiException("Opportunity not found");}

        review.setOpportunity(opportunity);
        review.setCreatedAt(LocalDateTime.now());

        reviewRepository.save(review);

        return "review added successfully";
    }

    //36
    public List<Review> getOpportunityReviewsForOrganization(Integer opportunityId, Integer organizationId) {
        Opportunity opportunity = opportunityRepository.findOpportunityById(opportunityId);
        if (opportunity==null){
            throw new ApiException("Opportunity not found");
        }

        if (!opportunity.getOrganization().getId().equals(organizationId)) {
            throw new RuntimeException("You do not have access to view evaluations for this opportunity");
        }

        return reviewRepository.findByOpportunityId(opportunityId);
    }


    public Double getAverageRating(Organization organization) {
        List<Review> reviews = reviewRepository.findAllByOrganizationId(organization.getId());

        if (reviews.isEmpty()) {
            throw new ApiException("No reviews found for this organization");
        }

        double total = 0;
        for (Review review : reviews) {
            total += review.getRating();
        }

        return total / reviews.size();
    }

    public Integer getReviewCount(Organization organization) {
        List<Review> reviews = reviewRepository.findAllByOrganizationId(organization.getId());
        return reviews.size();
    }

    public Double getOpportunityAverageRating(Integer opportunityId, Organization organization) {
        Opportunity opportunity = opportunityRepository.findOpportunityById(opportunityId);

        if (opportunity == null) {
            throw new ApiException("Opportunity not found");
        }

        if (!opportunity.getOrganization().getId().equals(organization.getId())) {
            throw new ApiException("Access denied: not your opportunity");
        }

        List<Review> reviews = reviewRepository.findAllByOpportunity(opportunity);

        if (reviews.isEmpty()) {
            return 0.0;
        }

        double sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }

        return sum / reviews.size();
    }

    public Integer getReviewCountForOpportunity(Integer opportunityId, Organization organization) {
        Opportunity opportunity = opportunityRepository.findOpportunityById(opportunityId);

        if (opportunity == null) {
            throw new ApiException("Opportunity not found");
        }

        if (!opportunity.getOrganization().getId().equals(organization.getId())) {
            throw new ApiException("Access denied: not your opportunity");
        }

        return reviewRepository.countByOpportunity(opportunity);
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




