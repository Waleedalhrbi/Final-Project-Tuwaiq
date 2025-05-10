package org.example.atharvolunteeringplatform.Repository;

import org.example.atharvolunteeringplatform.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    Review findReviewById(Integer id);

    //36
    List<Review> findByOpportunityId(Integer opportunityId);

}
