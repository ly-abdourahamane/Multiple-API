package com.mandat.amoulanfe.file;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadDTO {

    @NotBlank
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String type;

    @NotBlank
    private Long size;

    @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;
}
