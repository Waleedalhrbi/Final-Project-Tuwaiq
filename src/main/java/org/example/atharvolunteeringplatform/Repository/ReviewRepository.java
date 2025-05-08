package org.example.atharvolunteeringplatform.Repository;

import org.example.atharvolunteeringplatform.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    Review findReviewById(Integer id);
}
