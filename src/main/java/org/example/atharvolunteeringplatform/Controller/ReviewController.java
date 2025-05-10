package org.example.atharvolunteeringplatform.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Model.Review;
import org.example.atharvolunteeringplatform.Service.ReviewService;
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
//
//    @PostMapping("/add")
//    public ResponseEntity addReview(@RequestParam Integer opportunityId, @RequestParam Integer studentId,/*@AuthenticationPrincipal*/ @RequestParam Integer supervisorId,@RequestBody @Valid Review review) {
//        reviewService.addReview(opportunityId,studentId,review,supervisorId);
//        return ResponseEntity.ok("Review added successfully");
//    }

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

    //36
    @GetMapping("/get-review/{opportunityId}/organization/{organizationId}")
    public ResponseEntity getReviewForOpportunity(@PathVariable Integer opportunityId,/*@AuthenticationPrincipal*/@PathVariable Integer organizationId) {
        List<Review> review = reviewService.getOpportunityReviewsForOrganization(opportunityId, organizationId);
        if(review.isEmpty()) {
            return ResponseEntity.ok().body("No review found");
        }
        return ResponseEntity.ok(review);
    }

    //41
    @PostMapping("/review/{opportunityId}/school/{schoolId}")
    public ResponseEntity<String> evaluateOpportunity(@PathVariable Integer opportunityId, /*@AuthenticationPrincipal*/@PathVariable Integer schoolId, @RequestBody @Valid Review review) {
        return ResponseEntity.ok().body(reviewService.reviewOpportunity(opportunityId, schoolId,review));

    }
}
