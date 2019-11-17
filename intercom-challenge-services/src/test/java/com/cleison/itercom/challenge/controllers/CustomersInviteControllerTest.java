package com.cleison.itercom.challenge.controllers;

import com.cleison.itercom.challenge.domains.Customer;
import com.cleison.itercom.challenge.exeptions.FileStorageException;
import com.cleison.itercom.challenge.services.CustomersInviteService;
import com.cleison.itercom.challenge.services.FileStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(CustomersInviteController.class)
@WebAppConfiguration
public class CustomersInviteControllerTest {

    private final static String TEST_END_POINT = "/customers";

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    private List<Customer> customerList;
    private Path path;
    MockMultipartFile multipartFile;

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
        path = FileSystems.getDefault().getPath("src/main/resources/customers.txt").toAbsolutePath();
        InputStream inputStream = Files.newInputStream(path);
        multipartFile = new MockMultipartFile("file", "customers.txt", "text/plain", inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIfRequestBodyIsNull() throws Exception{
        multipartFile = null;
        getMvcResult(TEST_END_POINT);
    }

    @Test
    public void testIfFileListIsEmpty() throws Exception{
        path = FileSystems.getDefault().getPath("src/main/resources/customers_empty.txt").toAbsolutePath();
        InputStream inputStream = Files.newInputStream(path);
        multipartFile = new MockMultipartFile("file", "customer_empty.txt", "text/plain", inputStream);
        String result = getMvcResult(TEST_END_POINT).andReturn().getResponse().getContentAsString();
        Assert.assertTrue("Test if list is empty when file content is empty", "Empty list!".equals(result));
    }

    @Test(expected = NoSuchFileException.class)
    public void testIfFilePathIsIncorrect() throws Exception{
        path = FileSystems.getDefault().getPath("src/main/resources/file.txt").toAbsolutePath();
        InputStream inputStream = Files.newInputStream(path);
        multipartFile = new MockMultipartFile("file", "customer_empty.txt", "text/plain", inputStream);
        getMvcResult(TEST_END_POINT).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testIfListObjectReturnIsCorrect() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String result = getMvcResult(TEST_END_POINT).andReturn().getResponse().getContentAsString();
        customerList = new ArrayList<>();
        customerList = Arrays.asList(mapper.readValue(result, Customer[].class));
        Assert.assertEquals("Test if list customer has correct list size", 16, customerList.size());
    }

    @Test
    public void testStatusResponseIsOK() throws Exception {
        getMvcResult(TEST_END_POINT).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testContentTypeIsJson() throws Exception {
        getMvcResult(TEST_END_POINT).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testIfCustomerExist() throws Exception {
        ResultActions mvcResult = getMvcResult(TEST_END_POINT).andExpect(MockMvcResultMatchers.jsonPath("$[5].name", is(customerList.get(0).getName())));
    }

    private ResultActions getMvcResult(String endpoint)
            throws Exception {
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
