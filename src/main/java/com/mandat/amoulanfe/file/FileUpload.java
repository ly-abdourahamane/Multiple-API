package com.mandat.amoulanfe.file;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity(name = "Files")
public class FileUpload implements Serializable {

    @NotNull
    Long size;
    @NotNull
    String CreatedDate;
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
}
