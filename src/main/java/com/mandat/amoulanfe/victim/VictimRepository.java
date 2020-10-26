package com.mandat.amoulanfe.victim;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VictimRepository extends JpaRepository<Victim, Long>, JpaSpecificationExecutor<Victim> {
}
