package com.cleison.itercom.challenge.controllers;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void getCustomersToInviteTest() throws Exception {
        MvcResult mvcResult = getMvcResult(MockMvcResultMatchers.status().isOk(), TEST_ENDPOINT);
        String responseBody = mvcResult.getResponse().getContentAsString();
        log.info("getCustomersToInviteTest()");
        Assert.assertEquals("response", responseBody);
    }

    private MvcResult getMvcResult(ResultMatcher expectedStatus, String endpoint)
            throws Exception {
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(endpoint).contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON);
        return mockMvc.perform(requestBuilder).andExpect(expectedStatus).andReturn();
    }
}