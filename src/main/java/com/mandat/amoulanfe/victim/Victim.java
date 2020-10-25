package com.mandat.amoulanfe.victim;

import lombok.Data;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Data
@Entity(name = "victims")
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

    @NotBlank
    private String deathDate;

    @NotBlank
    private String birthDate;

    private int age;

    @Lob
    private byte[] profile;
}

