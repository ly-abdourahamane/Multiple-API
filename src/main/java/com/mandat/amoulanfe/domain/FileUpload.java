package com.mandat.amoulanfe.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity(name = "Files")
public class FileUpload implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private String type;

    @Lob
    private byte[] buffer;

    @NotNull
    Long size;

    @NotNull
    String CreatedDate;
}
