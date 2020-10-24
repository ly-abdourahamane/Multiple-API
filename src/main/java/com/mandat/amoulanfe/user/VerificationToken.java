package com.mandat.amoulanfe.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String token;
    private LocalDateTime dateExpiration;

    @OneToOne
    private User user;

    public VerificationToken(String token, LocalDateTime dateExpiration, Object object) {
        this.token = token;
        this.dateExpiration = dateExpiration;
        if (object instanceof User) {
            this.user = (User) object;
        }
    }

}