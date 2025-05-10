package org.example.atharvolunteeringplatform.Service;


import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Api.ApiException;
import org.example.atharvolunteeringplatform.DTO.OpportunityDTO;
import org.example.atharvolunteeringplatform.Model.Opportunity;
import org.example.atharvolunteeringplatform.Model.Organization;
import org.example.atharvolunteeringplatform.Repository.OpportunityRepository;
import org.example.atharvolunteeringplatform.Repository.OrganizationRepository;
import org.springframework.mail.MailSender;
 
import org.springframework.mail.SimpleMailMessage;
 
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpportunityService {

    private final OpportunityRepository opportunityRepository;
    private final OrganizationRepository organizationRepository;

 
    private final MailSender mailSender;
 



    public List<Opportunity> findAllOpportunities() {
        return opportunityRepository.findAll();
    }

    public void createOpportunity(Opportunity opportunity, Integer organizationId) {
        Organization organization = organizationRepository.findOrganizationById(organizationId);

        if (organization == null) {
            throw new ApiException("Organization not found");
        }

        if (organization.getStatus().equalsIgnoreCase("InActive")) {
            throw new ApiException("Organization status is InActive");
        }

        opportunity.setTitle(opportunity.getTitle());
        opportunity.setDescription(opportunity.getDescription());
        opportunity.setLocation(opportunity.getLocation());
        opportunity.setStatus("pending");
        opportunity.setTypeOpportunity(opportunity.getTypeOpportunity());
        opportunity.setHours(opportunity.getHours());
        opportunity.setGender(opportunity.getGender());
        opportunity.setStudentCapacity(opportunity.getStudentCapacity());
        opportunity.setStartDate(opportunity.getStartDate());
        opportunity.setEndDate(opportunity.getEndDate());
        opportunity.setCreatedAt(LocalDateTime.now());

        opportunity.setOrganization(organization);
        opportunityRepository.save(opportunity);
    }

    public void updateOpportunity(Integer opportunityId,Integer organizationId, Opportunity updatedOpportunity) {
        Opportunity oldOpportunity = opportunityRepository.findOpportunityById(opportunityId);
        Organization oldOrganization = organizationRepository.findOrganizationById(organizationId);

        if (oldOpportunity == null) {
            throw new ApiException("Opportunity not found");
        }
        if (oldOrganization == null) {
            throw new ApiException("Organization not found");
        }

        if (!oldOpportunity.getOrganization().getId().equals(oldOrganization.getId())) {
            throw new ApiException("Organization cannot update this opportunity");
        }


        oldOpportunity.setTitle(updatedOpportunity.getTitle());
        oldOpportunity.setTypeOpportunity(updatedOpportunity.getTypeOpportunity());
        oldOpportunity.setGender(updatedOpportunity.getGender());
        oldOpportunity.setDescription(updatedOpportunity.getDescription());
        oldOpportunity.setStartDate(updatedOpportunity.getStartDate());
        oldOpportunity.setEndDate(updatedOpportunity.getEndDate());
        oldOpportunity.setHours(updatedOpportunity.getHours());
        oldOpportunity.setStudentCapacity(updatedOpportunity.getStudentCapacity());
        oldOpportunity.setLocation(updatedOpportunity.getLocation());

        oldOpportunity.setStatus("pending");

        opportunityRepository.save(oldOpportunity);
    }

    public void deleteOpportunity(Integer opportunityId) {
        Opportunity opportunity = opportunityRepository.findOpportunityById(opportunityId);

        if (opportunity == null) {
            throw new ApiException("Opportunity not found");
        }

        opportunityRepository.delete(opportunity);
    }

    //4
    public List<Opportunity> getOpportunitiesSortedByCapacity(Integer organizationId) {
        return opportunityRepository.findByOrganizationIdOrderByStudentCapacityDesc(organizationId);
    }


    //21
    public int countByStatus(Integer organizationId, String status) {
        List<Opportunity> opportunities = opportunityRepository.findOpportunitiesByOrganizationId(organizationId);
        int count = 0;

        for (Opportunity o : opportunities) {
 
            if (o.getStatus().equalsIgnoreCase(status)) {
 
                count++;
            }
        }

        return count;
    }

    //21
    public int countTotalOpportunities(Integer organizationId) {
        List<Opportunity> opportunities = opportunityRepository.findOpportunitiesByOrganizationId(organizationId);
        return opportunities.size();
    }


    //25
    public List<Opportunity> getOpportunitiesByStatus(String status) {
        return opportunityRepository.findOpportunitiesByStatus(status);
    }

    //2
    // تحول كل فرصه الى DTO قبل لارجاع النتيجه لتفادي ظهور معلومات حساسه للطالب
    public List<OpportunityDTO> getOpenOpportunitiesByType(String typeOpportunity) {
        List<Opportunity> opportunities = opportunityRepository.findByTypeOpportunityAndStatus(typeOpportunity, "open");
        List<OpportunityDTO> dtoList = new ArrayList<>();

        for (Opportunity opportunity : opportunities) {
            OpportunityDTO dto = new OpportunityDTO(opportunity);
            dtoList.add(dto);
        }
        return dtoList;
    }

    //56
    //for Admin Only
//    public void approveOpportunity(Integer opportunityId) {
//        Opportunity opportunity = opportunityRepository.findOpportunityById(opportunityId);
//        if(opportunity==null){
//            throw  new ApiException("Opportunity not found");
//        }
//        if (!opportunity.getStatus().equalsIgnoreCase("pending")) {
//            throw new RuntimeException("Only pending opportunities can be approved");
//        }
//        opportunity.setStatus("open");
//        opportunityRepository.save(opportunity);
//    }

    //6
    public List<OpportunityDTO> getOpenOpportunitiesByLocation(String location) {
        List<Opportunity> opportunities = opportunityRepository.findByLocationAndStatus(location, "open");

        List<OpportunityDTO> dtoList = new ArrayList<>();
        for (Opportunity opportunity : opportunities) {
            dtoList.add(new OpportunityDTO(opportunity));
        }

        return dtoList;
    }

    //24
    //الفرص من الاحدث
    public List<Opportunity> getLatestOpportunitiesByOrganization(Integer organizationId) {
        return opportunityRepository.findByOrganizationIdOrderByCreatedAtDesc(organizationId);
    }

    //29
    public List<Opportunity> getOpportunitiesByOrganization(Integer organizationId) {
        return opportunityRepository.findByOrganizationId(organizationId);
    }


    // 27
    public void acceptOpportunity(Integer opportunityId) {
        Opportunity opportunity = opportunityRepository.findOpportunityById(opportunityId);
        if (opportunity == null) {
            throw new ApiException("Opportunity not found");
        }

        opportunity.setStatus("accepted");
        opportunityRepository.save(opportunity);

        String to = opportunity.getOrganization().getUser().getEmail();
        String subject = "تم قبول الفرصة التطوعية";
        String body = "تمت الموافقة على الفرصة التطوعية بعنوان: " + opportunity.getTitle();
        sendDecisionEmail(to, subject, body);
    }

    // 28
    public void acceptOpportunityEdit(Integer opportunityId) {
        Opportunity opportunity = opportunityRepository.findOpportunityById(opportunityId);
        if (opportunity == null) {
            throw new ApiException("Opportunity not found");
        }

        opportunity.setStatus("accepted");
        opportunityRepository.save(opportunity);

        String to = opportunity.getOrganization().getUser().getEmail();
        String subject = "تم قبول طلب تعديل الفرصة";
        String body = "تمت الموافقة على التعديلات المقترحة للفرصة التطوعية: " + opportunity.getTitle();
        sendDecisionEmail(to, subject, body);
    }

    // 61
    public void rejectOpportunity(Integer opportunityId) {
        Opportunity opportunity = opportunityRepository.findOpportunityById(opportunityId);
        if (opportunity == null) {
            throw new ApiException("Opportunity not found");
        }

        opportunity.setStatus("rejected");
        opportunityRepository.save(opportunity);

        String to = opportunity.getOrganization().getUser().getEmail();
        String subject = "تم رفض الفرصة التطوعية";
        String body = "نأسف، تم رفض الفرصة التطوعية بعنوان: " + opportunity.getTitle();
        sendDecisionEmail(to, subject, body);
    }

    // 62
    public void rejectOpportunityEdit(Integer opportunityId) {
        Opportunity opportunity = opportunityRepository.findOpportunityById(opportunityId);
        if (opportunity == null) {
            throw new ApiException("Opportunity not found");
        }

        opportunity.setStatus("rejected");
        opportunityRepository.save(opportunity);

        String to = opportunity.getOrganization().getUser().getEmail();
        String subject = "تم رفض طلب تعديل الفرصة";
        String body = "نأسف، تم رفض التعديلات المقترحة للفرصة التطوعية: " + opportunity.getTitle();
        sendDecisionEmail(to, subject, body);
    }

    // 63
    public void sendDecisionEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }




    //56
    public void changeOpportunityStatus(Integer opportunityId, String newStatus) {
        Opportunity opportunity = opportunityRepository.findOpportunityById(opportunityId);
        if (opportunity == null) {
            throw new ApiException("Opportunity not found");
        }

        String status = newStatus.toLowerCase();

        if (status.equals("open") || status.equals("pending") || status.equals("accepted")
                || status.equals("rejected") || status.equals("closed")) {
            opportunity.setStatus(status);
            opportunityRepository.save(opportunity);
        } else {
            throw new ApiException("Invalid status value");
        }
    }

}



