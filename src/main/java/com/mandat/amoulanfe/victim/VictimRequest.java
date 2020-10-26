package com.mandat.amoulanfe.victim;

import lombok.Data;

@Data
public class VictimRequest {
    private String firstname;
    private String lastname;
    private String city;
    private String description;
    private String deathDate;
    private String birthDate;
    private int age;
}
