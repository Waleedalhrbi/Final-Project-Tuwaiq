package org.example.atharvolunteeringplatform.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SchoolDTO {


    @Column(nullable = false)
    @NotEmpty(message = "Name cannot be Empty")
    private String name;

    private String email;

    private String phoneNumber;

    private String password;

    @Column(nullable = false)
    @NotEmpty(message = "Region cannot be Empty")
    private String region;

    @Column(nullable = false)
    @NotEmpty(message = "gender cannot be Empty")
    @Pattern(regexp = "Male|female")
    private String gender;

    @Column(nullable = false)
    @NotEmpty(message = "City cannot be Empty")
    private String city;

    @Column(nullable = false)
    @NotEmpty(message = "supervisor Name cannot be Empty")
    private String supervisorName;


}
