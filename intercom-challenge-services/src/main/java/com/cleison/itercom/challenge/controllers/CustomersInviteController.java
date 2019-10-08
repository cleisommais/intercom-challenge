package com.cleison.itercom.challenge.controllers;

import com.cleison.itercom.challenge.domains.Customer;
import com.cleison.itercom.challenge.services.CustomersInviteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class CustomersInviteController {
    @Autowired
    CustomersInviteService customersInviteService;

    @GetMapping(path = "/invite")
    public ResponseEntity getCustomersToInvite() {
        log.info("Running getCustomersToInvite()");
        List<Customer> list = customersInviteService.getCustomersListToInvite();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
