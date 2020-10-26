package com.mandat.amoulanfe.victim;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Victim implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String firstname;

    @NotBlank
    @Size(max = 40)
    private String lastname;

    @NotBlank
    @Size(max = 40)
    private String city;

    @NotBlank
    @Size(max = 5000)
    private String description;

    @NotNull
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime addedDate = LocalDateTime.now();

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate deathDate;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    private int age;

    private String profileLink;
}

