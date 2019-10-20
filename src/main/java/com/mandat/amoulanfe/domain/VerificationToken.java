package com.mandat.amoulanfe.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String token;
    private LocalDateTime dateExpiration;

    @OneToOne
    private User user;

    public VerificationToken(String token, LocalDateTime dateExpiration, Object object){
        this.token = token;
        this.dateExpiration = dateExpiration;
        if(object instanceof User){
            this.user = (User) object;
        }
    }

}