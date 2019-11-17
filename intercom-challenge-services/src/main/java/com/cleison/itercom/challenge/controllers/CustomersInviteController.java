package com.cleison.itercom.challenge.controllers;

import com.cleison.itercom.challenge.domains.Customer;
import com.cleison.itercom.challenge.exeptions.LogicExeption;
import com.cleison.itercom.challenge.services.CustomersInviteService;
import com.cleison.itercom.challenge.services.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@RestController
@Slf4j
public class CustomersInviteController {

    private final CustomersInviteService customersInviteService;
    private final FileStorageService storageService;

    public CustomersInviteController(@Autowired final CustomersInviteService customersInviteService, @Autowired FileStorageService storageService) {
        this.customersInviteService = customersInviteService;
        this.storageService = storageService;
    }

    @PostMapping(path = "/customers")
    public ResponseEntity getCustomersToInvite(@RequestPart("file") MultipartFile file) {
        log.info("Running getCustomersToInvite()");
        Path path = storageService.storeFile(file);
        List<Customer> list = null;
        try {
            list = customersInviteService.getCustomersListToInvite(path);
            if (list != null && !list.isEmpty())
                return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (LogicExeption e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Empty list!", HttpStatus.OK);
    }
}
