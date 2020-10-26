package com.mandat.amoulanfe.file;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity(name = "Files")
public class FileUpload implements Serializable {

    @NotNull
    Long size;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime creationDate = LocalDateTime.now();
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
