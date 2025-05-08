package org.example.atharvolunteeringplatform.Repository;

import org.example.atharvolunteeringplatform.Model.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Integer> {

    Opportunity findOpportunityById(Integer id);
}
