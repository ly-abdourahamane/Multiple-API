package com.mandat.amoulanfe.repository;

import com.mandat.amoulanfe.domain.FileDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileDomain, Long> {

    @Query(value = "SELECT f FROM FileDomain f")
    List<FileDomain> getAllFiles();

    @Query(value = "SELECT f FROM FileDomain f WHERE f.name = :name")
    FileDomain findByName(@Param("name") String name);

    Boolean existsByName(String name);
}
