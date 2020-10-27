package com.mandat.amoulanfe.victim;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class VictimFilter {
    private String firstname;
    private String lastname;
    private String city;
    private String description;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate deathFromDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate deathToDate;

    private String search;
}
