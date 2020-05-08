package com.mandat.amoulanfe.repository;

import com.mandat.amoulanfe.domain.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileUpload, Long> {

    @Query(value = "SELECT f FROM Files f")
    List<FileUpload> getAllFiles();

    Optional<FileUpload> findByName(String name);

    Optional<Boolean> existsByName(String name);
}
