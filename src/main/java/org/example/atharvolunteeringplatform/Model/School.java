package org.example.atharvolunteeringplatform.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @Column(nullable = false)
//    @NotEmpty(message = "Name cannot be Empty")
//    private String name;

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
//
//    @OneToOne
//    private User user;
//
//    @OneToMany
//    private Set<Student>students;
//
//    @OneToMany
//    private Set<Review>reviews;

}
