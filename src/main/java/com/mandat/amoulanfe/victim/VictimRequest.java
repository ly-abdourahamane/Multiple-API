package com.mandat.amoulanfe.victim;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class VictimRequest {
    private String firstname;
    private String lastname;
    private String city;
    private String description;

    @JsonProperty("birthDate")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    @JsonProperty("deathDate")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate deathDate;

    private int age;
}
