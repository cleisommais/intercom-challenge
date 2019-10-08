package com.cleison.itercom.challenge.services;

import com.cleison.itercom.challenge.domains.Customer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Component
public class CustomersInviteService {
    private static final int EARTH_RADIUS = 6371;
    private static final double START_LATITUTE = 53.3381985;
    private static final double START_LONGITUDE = -6.2592576;
    @Autowired
    FileService fileService;

    public List<Customer> getListFromFileAndTransformToCustomersList() {
        List<Customer> customerList = new ArrayList<>();
        Customer customer = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            for (Object data : fileService.getCustomersDataFromFile()) {
                JsonNode jsonNode = null;
                jsonNode = mapper.readTree((String) data);
                customer = new Customer(jsonNode.get("name").asText(), jsonNode.get("user_id").asInt(), jsonNode.get("latitude").asDouble(), jsonNode.get("longitude").asDouble());
                customerList.add(customer);
            }
        } catch (IOException io) {
            log.error("Error when mapper read json tree {}", io.getMessage());
        } catch (Exception e) {
            log.error("Error {}", e.getMessage());
        }
        return customerList;
    }

    public List<Customer> getCustomersListToInvite() {
        List<Customer> customerList = new ArrayList<>();
        for (Customer customer : getListFromFileAndTransformToCustomersList()) {
            double distance = distanceInKm(START_LATITUTE, START_LONGITUDE, customer.getLatitude(), customer.getLongitude());
            if (distance <= 100)
                customerList.add(customer);
        }
        Collections.sort(customerList, Comparator.comparing(Customer::getUserId));
        return customerList;
    }

    public int numberOfInvitedCustomersBetweenZeroAndOneHundredKm(List<Customer> customerList) {
        //dublin  53.3381985, -6.2592576
        int count = 0;
        for (Customer customer : customerList) {
            double distance = distanceInKm(START_LATITUTE, START_LONGITUDE, customer.getLatitude(), customer.getLongitude());
            if (distance <= 100)
                count++;
        }
        return count;
    }

    private double distanceInKm(double startLati, double startLong, double endLati, double endLong) {
        //degrees to radius
        double diffLati = Math.toRadians(endLati - startLati);
        double diffLong = Math.toRadians(endLong - startLong);
        double radiusStartLati = Math.toRadians(startLati);
        double radiusEndLati = Math.toRadians(endLati);
        // A and C are the 'sides' from the spherical triangle.
        double a = Math.pow(Math.sin(diffLati / 2), 2) + Math.pow(Math.sin(diffLong / 2), 2) * Math.cos(radiusStartLati) * Math.cos(radiusEndLati);
        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS * c;
    }
}
