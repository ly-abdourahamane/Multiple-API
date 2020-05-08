package com.mandat.amoulanfe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
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
