package com.cleison.itercom.challenge.services;

import com.cleison.itercom.challenge.domains.Customer;
import com.cleison.itercom.challenge.exeptions.LogicExeption;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
	public List<Customer> getListFromFileAndTransformToCustomersList(Path path) throws IOException {
        List<Customer> customerList = new ArrayList<>();
        Customer customer = null;
        ObjectMapper mapper = new ObjectMapper();
        for (Object data : fileStorageService.getDataFromFile(path)) {
            JsonNode jsonNode = null;
            jsonNode = mapper.readTree((String) data);
			if (isJsonCorrectFormat(jsonNode)) {
				customer = new Customer(jsonNode.get("name").asText(), jsonNode.get("user_id").asInt(), jsonNode.get("latitude").asDouble(), jsonNode.get("longitude").asDouble(), null);
				customerList.add(customer);
			} else {
				throw new IllegalArgumentException();
			}
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
		} catch (JsonParseException | IllegalArgumentException e) {
            throw new LogicExeption("The data format is incorrect!", e);
        } catch (IOException e) {
            throw new LogicExeption(e.getMessage(), e);
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
        double startLatiInRadios = Math.toRadians(startLati);
        double startLongInRadios = Math.toRadians(startLong);
        double endLatiInRadios = Math.toRadians(endLati);
        double endLongInRadios = Math.toRadians(endLong);
        //Distance, d = EARTH_RADIUS_IN_KM * arccos[(sin(lat1) * sin(lat2)) + cos(lat1) * cos(lat2) * cos(long2 – long1)]
        distanceBetweenAandBInKm = EARTH_RADIUS_IN_KM * Math.acos(
            Math.sin(startLatiInRadios) * Math.sin(endLatiInRadios) +
                Math.cos(startLatiInRadios) * Math.cos(endLatiInRadios) *
                    Math.cos(endLongInRadios - startLongInRadios));
        return distanceBetweenAandBInKm;
    }

	/**
	 * Check if each line read has the correct format
	 * @param jsonNode
	 * @return
	 */
	private boolean isJsonCorrectFormat(JsonNode jsonNode) {
		if (jsonNode.findValues("name").isEmpty() ||
			jsonNode.findValues("user_id").isEmpty() ||
			jsonNode.findValues("latitude").isEmpty() ||
			jsonNode.findValues("longitude").isEmpty()) {
			return false;
		}
		return true;
	}
}
