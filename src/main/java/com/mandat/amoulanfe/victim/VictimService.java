package com.mandat.amoulanfe.victim;

import com.mandat.amoulanfe.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VictimService {

    @Autowired
    private VictimRepository victimRepository;

    private UserRepository userRepository;

    public Long save(VictimRequest victimRequest) {
        Victim victim = new Victim();
        victim.setAge(victimRequest.getAge());
        victim.setCity(victimRequest.getCity());
        victim.setBirthDate(victimRequest.getBirthDate());
        victim.setDescription(victimRequest.getDescription());
        victim.setFirstname(victimRequest.getFirstname());
        victim.setLastname(victimRequest.getLastname());
        victim.setDeathDate(victimRequest.getDeathDate());

        Victim victimSaved = victimRepository.save(victim);
        return victimSaved.getId();
    }

    public Victim getVictimById(Long id) {
        return victimRepository.getOne(id);
    }

    public Page<Victim> findAllVictims(Specification<Victim> specification, Pageable pageable) {
        return victimRepository.findAll(specification, pageable);
    }
}
