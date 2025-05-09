package org.example.atharvolunteeringplatform.Service;

import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Api.ApiException;
import org.example.atharvolunteeringplatform.Model.Opportunity;
import org.example.atharvolunteeringplatform.Model.Organization;
import org.example.atharvolunteeringplatform.Model.Review;
import org.example.atharvolunteeringplatform.Repository.OpportunityRepository;
import org.example.atharvolunteeringplatform.Repository.OrganizationRepository;
import org.example.atharvolunteeringplatform.Repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrganizationRepository organizationRepository;
    private final OpportunityRepository opportunityRepository;
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public void addReview(Review review) {
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);
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
//    }


    // 20
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

    //35
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



















