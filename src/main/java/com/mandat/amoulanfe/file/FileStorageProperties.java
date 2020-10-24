package com.mandat.amoulanfe.file;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "file-storage")
public class FileStorageProperties {
    private String uploadDir;
}