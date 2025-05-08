package org.example.atharvolunteeringplatform.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class Complaints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotEmpty(message = "Title cannot be Empty")
    @Max(value = 30,message = "Title cannot exceed  than 30 characters")

    private String title;

    @Column(nullable = false)
    @NotEmpty(message = "description cannot be Empty")
    @Max(value = 300,message = "description cannot exceed  than 300 characters")
    private String description;

    @Pattern(regexp = "^(In Progress|Resolved|Closed)$")
    private String status;

    private LocalDateTime CreateAt;

//    @ManyToOne
//    private Student students;
    @ManyToOne
    private Opportunity opportunity ;

}
