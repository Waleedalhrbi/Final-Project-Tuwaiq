package org.example.atharvolunteeringplatform.Service;

import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Api.ApiException;
import org.example.atharvolunteeringplatform.DTO.OrganizationDTO;
import org.example.atharvolunteeringplatform.Model.MyUser;
import org.example.atharvolunteeringplatform.Model.Organization;
import org.example.atharvolunteeringplatform.Repository.MyUserRepository;
import org.example.atharvolunteeringplatform.Repository.OrganizationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final MyUserRepository userRepository;


    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    public void createOrganization(OrganizationDTO organizationDTO) {
        MyUser user = new MyUser();

        user.setName(organizationDTO.getName());
        user.setEmail(organizationDTO.getEmail());
        user.setPassword(organizationDTO.getPassword());
        user.setPhone_number(organizationDTO.getPhoneNumber());
        user.setRole("ORGANIZATION");
        user.setCreated_at(LocalDateTime.now());

        Organization organization = new Organization();

        organization.setStatus("InActive");
        organization.setUser(user);
        user.setOrganization(organization);
        organizationRepository.save(organization);
        userRepository.save(user);
    }

    public void updateOrganization(Integer organizationId , OrganizationDTO organizationDTO) {
        MyUser oldUser = userRepository.findMyUserById(organizationId);
        Organization oldOrganization = organizationRepository.findOrganizationById(organizationId);

        if (oldUser == null || oldOrganization == null) {
            throw new ApiException("Organization not found");
        }
        oldUser.setName(organizationDTO.getName());
        oldUser.setEmail(organizationDTO.getEmail());
        oldUser.setPhone_number(organizationDTO.getPhoneNumber());
        oldUser.setPassword(organizationDTO.getPassword());
        userRepository.save(oldUser);

        oldOrganization.setDescription(oldOrganization.getDescription());
        oldOrganization.setLicense(oldOrganization.getLicense());
        oldOrganization.setLocation(oldOrganization.getLocation());

        organizationRepository.save(oldOrganization);

    }

    public void deleteOrganization(Integer organizationId) {
        Organization organization = organizationRepository.findOrganizationById(organizationId);
        if (organization == null) {
            throw new ApiException("Organization not found");
        }
        organizationRepository.delete(organization);
    }


}
