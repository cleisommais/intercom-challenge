package com.cleison.itercom.challenge.controllers;

import com.cleison.itercom.challenge.domains.Customer;
import com.cleison.itercom.challenge.services.CustomersInviteService;
import com.cleison.itercom.challenge.services.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(CustomersInviteController.class)
@WebAppConfiguration
public class CustomersInviteControllerTest {

    private final static String TEST_ENDPOINT = "/customers";

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    public List<Customer> customerList;
    Path path;

    @Before
    public void setUp() throws Exception {
        customerList = new ArrayList<>();
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        Customer customer = new Customer();
        customer.setLatitude(52.986375);
        customer.setLongitude(-6.043701);
        customer.setName("Christina McArdle");
        customer.setUserId(12);
        customer.setTargetDistance(99.0);
        customerList.add(customer);
    }

    @Test
    public void testStatusResponseIsOK() throws Exception {
        getMvcResult(TEST_ENDPOINT).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testContentTypeIsJson() throws Exception {
        getMvcResult(TEST_ENDPOINT).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testRetrieveObjectJson() throws Exception {
        ResultActions mvcResult = getMvcResult(TEST_ENDPOINT);
        String responseBody = mvcResult.andReturn().getResponse().getContentAsString();
        log.info("getCustomersToInviteTest()");
        Assert.assertEquals("[{\"name\":\"Ian Kehoe\",\"userId\":4,\"latitude\":53.2451022,\"longitude\":-6.238335,\"targetDistance\":10.457613454047323},{\"name\":\"Nora Dempsey\",\"userId\":5,\"latitude\":53.1302756,\"longitude\":-6.2397222,\"targetDistance\":23.1848516613069},{\"name\":\"Theresa Enright\",\"userId\":6,\"latitude\":53.1229599,\"longitude\":-6.2705202,\"targetDistance\":23.97449371678787},{\"name\":\"Eoin Ahearn\",\"userId\":8,\"latitude\":54.0894797,\"longitude\":-6.18671,\"targetDistance\":83.77739617947312},{\"name\":\"Richard Finnegan\",\"userId\":11,\"latitude\":53.008769,\"longitude\":-6.1056711,\"targetDistance\":38.08083415196093},{\"name\":\"Christina McArdle\",\"userId\":12,\"latitude\":52.986375,\"longitude\":-6.043701,\"targetDistance\":41.727863949787306},{\"name\":\"Olive Ahearn\",\"userId\":13,\"latitude\":53.0,\"longitude\":-7.0,\"targetDistance\":62.14089945369773},{\"name\":\"Michael Ahearn\",\"userId\":15,\"latitude\":52.966,\"longitude\":-6.463,\"targetDistance\":43.61280232848805},{\"name\":\"Patricia Cahill\",\"userId\":17,\"latitude\":54.180238,\"longitude\":-5.920898,\"targetDistance\":96.35369060525623},{\"name\":\"Eoin Gallagher\",\"userId\":23,\"latitude\":54.080556,\"longitude\":-6.361944,\"targetDistance\":82.92395377255883},{\"name\":\"Rose Enright\",\"userId\":24,\"latitude\":54.133333,\"longitude\":-6.433333,\"targetDistance\":89.26224524243297},{\"name\":\"Stephen McArdle\",\"userId\":26,\"latitude\":53.038056,\"longitude\":-7.653889,\"targetDistance\":98.8505549738122},{\"name\":\"Oliver Ahearn\",\"userId\":29,\"latitude\":53.74452,\"longitude\":-7.11167,\"targetDistance\":72.29402698848413},{\"name\":\"Nick Enright\",\"userId\":30,\"latitude\":53.761389,\"longitude\":-7.2875,\"targetDistance\":82.73596275421356},{\"name\":\"Alan Behan\",\"userId\":31,\"latitude\":53.1489345,\"longitude\":-6.8422408,\"targetDistance\":44.186892717091176},{\"name\":\"Lisa Ahearn\",\"userId\":39,\"latitude\":53.0033946,\"longitude\":-6.3877505,\"targetDistance\":38.24769543553712}]", responseBody);
    }

    @Test
    public void testIfCustomerExist() throws Exception {
        ResultActions mvcResult = getMvcResult(TEST_ENDPOINT).andExpect(MockMvcResultMatchers.jsonPath("$[5].name", is(customerList.get(0).getName())));
    }

    private ResultActions getMvcResult(String endpoint)
            throws Exception {
        path = FileSystems.getDefault().getPath("src\\main\\resources\\customers.txt").toAbsolutePath();
        InputStream inputStream = Files.newInputStream(path);
        MockMultipartFile multipartFile = new MockMultipartFile("file", "customers.txt", "text/plain", inputStream);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.multipart(endpoint).file(multipartFile);
        return mockMvc.perform(requestBuilder);
    }

    @TestConfiguration
    static class CustomersTestContextConfiguration {
        @Bean
        public CustomersInviteService customersInviteService() {
            return new CustomersInviteService(new FileStorageService());
        }

        @Bean
        public FileStorageService fileStorageService() {
            return new FileStorageService();
        }
    }

}
