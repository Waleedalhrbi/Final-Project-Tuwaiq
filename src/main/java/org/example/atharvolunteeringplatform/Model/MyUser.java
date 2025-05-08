package org.example.atharvolunteeringplatform.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class MyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotEmpty(message = "Please enter the student name")
    @Size(min = 2, max = 50, message = "Student name must be between 2 and 50 characters")
    @Column(columnDefinition = "varchar(50) not null")
    private String name;

    @NotEmpty(message = "Please enter an email")
    @Email(message = "Invalid email format")
    @Column(columnDefinition = "varchar(100) not null unique")
    private String email;

    @NotEmpty(message = "Please enter a phone number")
    @Pattern(regexp = "^05\\d{8}$", message = "Phone number must start with 05 and be 10 digits long")
    @Column(columnDefinition = "varchar(10) not null unique")
    private String phone_number;

    @NotEmpty(message = "Please enter a password")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{9,}$", message = "Password must be at least 9 characters and include letters and numbers")
    @Column(columnDefinition = "varchar(255) not null")
    private String password;

    @NotEmpty(message = "Please select a role")
    @Pattern(regexp = "^(student|supervisor|organization|admin)$", message = "Role must be one of: student, supervisor, organization, admin")
    @Column(columnDefinition = "varchar(20) not null")
    private String role;


    private LocalDateTime created_at;


    @OneToOne(cascade = CascadeType.ALL,mappedBy = "userStudent")
    private Student userStudent;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    @PrimaryKeyJoinColumn
    private Organization organization;
}
