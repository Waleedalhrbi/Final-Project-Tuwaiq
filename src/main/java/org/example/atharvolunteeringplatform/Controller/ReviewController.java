package org.example.atharvolunteeringplatform.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Api.ApiResponse;
import org.example.atharvolunteeringplatform.Model.MyUser;
import org.example.atharvolunteeringplatform.Model.Organization;
import org.example.atharvolunteeringplatform.Model.Review;
import org.example.atharvolunteeringplatform.Service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/all")
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }


    @PostMapping("/add-review/opportunity/{opportunityId}/school/{schoolId}")
    public ResponseEntity addReview(@PathVariable Integer opportunityId, @PathVariable Integer schoolId/*@AuthenticationPrincipal*/,@RequestBody @Valid Review review) {
        reviewService.createReview(review, schoolId, opportunityId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Review added successfully"));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer id) {
        reviewService.deleteReview(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Review deleted successfully"));
    }

    @DeleteMapping("/delete/{reviewId}/schoolId")
    public ResponseEntity<?> schoolDeleteReview(@PathVariable Integer reviewId, @PathVariable Integer schoolId) {
        reviewService.schoolDeleteReview(reviewId,schoolId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Review deleted successfully"));
    }

    //36
    @GetMapping("/get-review/{opportunityId}/organization/{organizationId}")
    public ResponseEntity getReviewForOpportunity(@PathVariable Integer opportunityId,/*@AuthenticationPrincipal*/@PathVariable Integer organizationId) {
        List<Review> review = reviewService.getOpportunityReviewsForOrganization(opportunityId, organizationId);
        if(review.isEmpty()) {
            return ResponseEntity.ok().body("No review found");
        }
        return ResponseEntity.ok(review);
    }

    @GetMapping("/organization/average-rating")
    public ResponseEntity getOrganizationAverageRating(/*@AuthenticationPrincipal*/ Organization organization) {
        return ResponseEntity.ok(reviewService.getAverageRating(organization));
    }

    @GetMapping("/organization/review-count")
    public ResponseEntity getOrganizationReviewCount(/*@AuthenticationPrincipal*/ Organization organization) {
        return ResponseEntity.ok(reviewService.getReviewCount(organization));
    }


    @GetMapping("/opportunity/{opportunityId}/average-rating")
    public ResponseEntity getOpportunityAverageRating(@PathVariable Integer opportunityId, /*@AuthenticationPrincipal*/ Organization organization) {
        return ResponseEntity.ok(reviewService.getOpportunityAverageRating(opportunityId, organization));
    }

    @GetMapping("/opportunity/{opportunityId}/review-count")
    public ResponseEntity getOpportunityReviewCount(@PathVariable Integer opportunityId, /*@AuthenticationPrincipal*/ Organization organization) {
        return ResponseEntity.ok(reviewService.getReviewCountForOpportunity(opportunityId, organization));
    }


}
