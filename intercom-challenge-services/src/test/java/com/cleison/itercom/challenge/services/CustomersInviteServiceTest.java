package com.cleison.itercom.challenge.services;

import com.cleison.itercom.challenge.domains.Customer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CustomersInviteServiceTest {

    private List<Customer> customerList;

    @Before
    public void setUp() throws Exception {
        customerList = new ArrayList<>();
        Path path = FileSystems.getDefault().getPath("src\\main\\resources\\customers.txt").toAbsolutePath();
        List list = Files.readAllLines(path);
        Customer customer = null;
        ObjectMapper mapper = new ObjectMapper();
        for (Object data : list) {
            JsonNode jsonNode = mapper.readTree((String) data);
            customer = new Customer(jsonNode.get("name").asText(), jsonNode.get("user_id").asInt(), jsonNode.get("latitude").asDouble(), jsonNode.get("longitude").asDouble());
            customerList.add(customer);
        }
        log.info(String.valueOf(customerList));
    }

    @Test
    public void numberOfInvitedCustomersBetweenZeroAndOneHundredKm() {
        CustomersInviteService inviteClosestCustomers = new CustomersInviteService();
        Assert.assertEquals(16, inviteClosestCustomers.numberOfInvitedCustomersBetweenZeroAndOneHundredKm(customerList));
    }
}