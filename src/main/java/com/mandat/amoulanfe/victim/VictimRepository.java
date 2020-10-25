package com.mandat.amoulanfe.victim;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VictimRepository extends JpaRepository<Victim, UUID>, JpaSpecificationExecutor<Victim> {}
