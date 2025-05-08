package org.example.atharvolunteeringplatform.DTO;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor

public class OrganizationDTO {

    private String email;


    private String name;


    private String phoneNumber;


    private String password;
}
