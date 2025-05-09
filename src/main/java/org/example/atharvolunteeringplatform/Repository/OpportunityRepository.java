package org.example.atharvolunteeringplatform.Repository;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.example.atharvolunteeringplatform.Model.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Integer> {

    Opportunity findOpportunityById(Integer id);

    @Query("SELECT o FROM Opportunity o WHERE o.organization.id = ?1 AND o.status = 'open'")
    List<Opportunity> findOpenOpportunitiesByOrganizationId(Integer organizationId);

    List<Opportunity> findOpportunitiesByOrganizationId(Integer organizationId);


    List<Opportunity> findByOrganizationIdOrderByStudentCapacityDesc(Integer organizationId);

    List<Opportunity> findOpportunitiesByStatus( String status);


}
