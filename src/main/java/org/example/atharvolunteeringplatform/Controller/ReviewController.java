package org.example.atharvolunteeringplatform.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/add")
    public ResponseEntity addReview(@RequestParam Integer opportunityId, @RequestParam Integer studentId,/*@AuthenticationPrincipal*/ @RequestParam Integer supervisorId,@RequestBody @Valid Review review) {
        reviewService.addReview(opportunityId,studentId,review,supervisorId);
        return ResponseEntity.ok("Review added successfully");
    }

//    @PutMapping("/update/{id}")
//    public ResponseEntity<String> updateReview(@PathVariable Integer id, @RequestBody @Valid Review review) {
//        reviewService.updateReview(id, review);
//        return ResponseEntity.ok("Review updated successfully");
//    }

//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> deleteReview(@PathVariable Integer id) {
//        reviewService.deleteReview(id);
//        return ResponseEntity.ok("Review deleted successfully");
//    }


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
