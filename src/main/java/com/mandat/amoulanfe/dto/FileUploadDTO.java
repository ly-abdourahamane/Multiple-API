package com.mandat.amoulanfe.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

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
    private String createdDate;
}
