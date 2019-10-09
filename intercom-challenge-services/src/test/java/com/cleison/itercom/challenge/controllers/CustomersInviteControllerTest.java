package com.cleison.itercom.challenge.controllers;

import com.cleison.itercom.challenge.domains.Customer;
import com.cleison.itercom.challenge.services.CustomersInviteService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(CustomersInviteController.class)
@WebAppConfiguration
public class CustomersInviteControllerTest {

    private final static String TEST_ENDPOINT = "/invite";
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private CustomersInviteService customersInviteService;
    private MockMvc mockMvc;
    public List<Customer> customerList;

    @Before
    public void setUp() throws Exception {
        customerList = new ArrayList<>();
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        Customer customer = new Customer();
        customer.setLatitude(52.986375);
        customer.setLongitude(-6.043701);
        customer.setName("Christina McArdle");
        customer.setUserId(12);
        customerList.add(customer);
    }

    @Test
    public void getCustomersToInviteTest() throws Exception {
        Mockito.when(customersInviteService.getCustomersListToInvite()).thenReturn(customerList);
        MvcResult mvcResult = getMvcResult(MockMvcResultMatchers.status().isOk(), TEST_ENDPOINT);
        String responseBody = mvcResult.getResponse().getContentAsString();
        log.info("getCustomersToInviteTest()");
        Assert.assertEquals("[{\"name\":\"Christina McArdle\",\"userId\":12,\"latitude\":52.986375,\"longitude\":-6.043701}]", responseBody);
    }

    private MvcResult getMvcResult(ResultMatcher expectedStatus, String endpoint)
            throws Exception {
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(endpoint).contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON);
        return mockMvc.perform(requestBuilder).andExpect(expectedStatus).andReturn();
    }
}
