package org.example.atharvolunteeringplatform.Service;

import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Model.Review;
import org.example.atharvolunteeringplatform.Repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

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


}



















