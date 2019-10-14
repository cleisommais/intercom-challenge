package com.cleison.itercom.challenge.services;

import com.cleison.itercom.challenge.domains.Customer;
import com.cleison.itercom.challenge.exeptions.LogicExeption;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class CustomersInviteService {
    private static final double EARTH_RADIUS_IN_KM = 6378.8;
    private static final double START_LATITUTE = 53.3381985;
    private static final double START_LONGITUDE = -6.2592576;
    private static final int DISTANCE_TARGET_IN_KM = 100;
    private final FileStorageService fileStorageService;

    public CustomersInviteService(@Autowired final FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    /**
     * Retrieved data from file and convert using reflexion to Customer object
     *
     * @param path
     * @return
     */
    public List<Customer> getListFromFileAndTransformToCustomersList(Path path) {
        List<Customer> customerList = new ArrayList<>();
        Customer customer = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            for (Object data : fileStorageService.getDataFromFile(path)) {
                JsonNode jsonNode = null;
                jsonNode = mapper.readTree((String) data);
                customer = new Customer(jsonNode.get("name").asText(), jsonNode.get("user_id").asInt(), jsonNode.get("latitude").asDouble(), jsonNode.get("longitude").asDouble(), null);
                customerList.add(customer);
            }
        } catch (Exception e) {
            throw new LogicExeption("Error when try build the reflexion between object list file to object Customer, probably the data format is incorrect!", e);
        }
        return customerList;
    }

    /**
     * Generate the list with the customers able to receive the invite
     *
     * @param path
     * @return
     */
    public List<Customer> getCustomersListToInvite(Path path) {
        List<Customer> customerList = new ArrayList<>();
        try {
            for (Customer customer : getListFromFileAndTransformToCustomersList(path)) {
                double distance = shortestDistanceInKm(START_LATITUTE, START_LONGITUDE, customer.getLatitude(), customer.getLongitude());
                customer.setTargetDistance(distance);
                if (distance <= DISTANCE_TARGET_IN_KM)
                    customerList.add(customer);
            }
            Collections.sort(customerList, Comparator.comparing(Customer::getUserId));
        } catch (Exception e) {
            throw new LogicExeption("Error when try set target distance more than " + DISTANCE_TARGET_IN_KM + ".", e);
        }
        return customerList;
    }

    /**
     * Method to calculate the shortest distance between two points
     *
     * @param startLati
     * @param startLong
     * @param endLati
     * @param endLong
     * @return
     */
    public double shortestDistanceInKm(double startLati, double startLong, double endLati, double endLong) {
        //degrees to radius
        double distanceBetweenAandBInKm = 0;
        try {
            double startLatiInRadios = Math.toRadians(startLati);
            double startLongInRadios = Math.toRadians(startLong);
            double endLatiInRadios = Math.toRadians(endLati);
            double endLongInRadios = Math.toRadians(endLong);
            //Distance, d = EARTH_RADIUS_IN_KM * arccos[(sin(lat1) * sin(lat2)) + cos(lat1) * cos(lat2) * cos(long2 – long1)]
            distanceBetweenAandBInKm = EARTH_RADIUS_IN_KM * Math.acos(
                    Math.sin(startLatiInRadios) * Math.sin(endLatiInRadios) +
                            Math.cos(startLatiInRadios) * Math.cos(endLatiInRadios) *
                                    Math.cos(endLongInRadios - startLongInRadios));
        } catch (Exception e) {
            throw new LogicExeption("Error when try calculate the shortest distance between two places.", e);
        }
        return distanceBetweenAandBInKm;
    }
}
