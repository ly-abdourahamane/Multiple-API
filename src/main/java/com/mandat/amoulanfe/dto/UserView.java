package com.mandat.amoulanfe.dto;

import lombok.Data;

@Data
public class UserView {
    private Long id;
    private String name;
    private String email;
    private String country;
    private String city;
}
