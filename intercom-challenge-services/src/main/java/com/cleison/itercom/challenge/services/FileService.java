package com.cleison.itercom.challenge.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Component
public class FileService {

    public List getCustomersDataFromFile() throws Exception {
        List list = null;
        try {
            Path path = FileSystems.getDefault().getPath("src\\main\\resources\\customers.txt").toAbsolutePath();
            list = Files.readAllLines(path);
        } catch (IOException io) {
            log.error("Error when try get Customers Data From File {}", io.getMessage());
        }
        return list;
    }
}
