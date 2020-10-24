package com.mandat.amoulanfe.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "file-storage")
public class FileStorageProperties {
    private String uploadDir;
}