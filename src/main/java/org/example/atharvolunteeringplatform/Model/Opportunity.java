package org.example.atharvolunteeringplatform.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor

public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotEmpty(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotEmpty(message = "Opportunity type is required")
    private String typeOpportunity;

    @NotEmpty(message = "Target gender is required")
    private String gender;

    @NotEmpty(message = "Description is required")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Hours are required")
    @Min(value = 1, message = "Hours must be at least 1")
    private Integer hours;

    @NotNull(message = "Student capacity is required")
    @Min(value = 1, message = "Student capacity must be at least 1")
    private Integer studentCapacity;

    @NotEmpty(message = "Location is required")
    private String location;

    @NotEmpty(message = "Status is required")
    private String status;

    @ManyToOne
    @JsonIgnore
    private Organization organization;

//***
    @OneToMany(mappedBy = "opportunity", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Complaint> complaints;

    //****
    @OneToMany(mappedBy = "opportunity", cascade = CascadeType.ALL)
    private Set<StudentOpportunityRequest> studentOpportunityRequests;
}
