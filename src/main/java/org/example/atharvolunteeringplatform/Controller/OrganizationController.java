package org.example.atharvolunteeringplatform.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.atharvolunteeringplatform.Api.ApiResponse;
import org.example.atharvolunteeringplatform.DTO.OrganizationDTO;
import org.example.atharvolunteeringplatform.Model.MyUser;
import org.example.atharvolunteeringplatform.Service.OrganizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping("/get-all")
    public ResponseEntity getAllOrganizations() {
        return ResponseEntity.status(HttpStatus.OK).body(organizationService.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity addOrganization(@RequestBody @Valid OrganizationDTO organizationDTO) {
        organizationService.createOrganization(organizationDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Organization added successfully"));
    }

    @PutMapping("/update")
    public ResponseEntity updateOrganization(
            /*@AuthenticationPrincipal*/ MyUser myUser,
                                         @RequestBody @Valid OrganizationDTO organizationDTO) {
        organizationService.updateOrganization(myUser.getId(), organizationDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Organization updated successfully"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteOrganization(/*@AuthenticationPrincipal*/ MyUser myUser) {
        organizationService.deleteOrganization(myUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Organization deleted successfully"));
    }
}
