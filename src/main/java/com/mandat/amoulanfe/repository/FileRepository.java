package com.mandat.amoulanfe.repository;

import com.mandat.amoulanfe.domain.FileDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileDomain, Long> {

    @Query(value = "SELECT f FROM FileDomain f")
    List<FileDomain> getAllFiles();

    Optional<FileDomain> findByName(String name);

    Optional<Boolean> existsByName(String name);
}
