package com.cleison.itercom.challenge.services;

import com.cleison.itercom.challenge.domains.Customer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CustomersInviteServiceTest {

    private List<Customer> customerList;
    CustomersInviteService customersInviteService;
    FileStorageService fileStorageService;
    Path path;
    private List<Object> objectList;

    @Before
    public void setUp() {
        fileStorageService = Mockito.mock(FileStorageService.class);
        customersInviteService = new CustomersInviteService(fileStorageService);
        objectList = new ArrayList<>();
        objectList.add("{\"latitude\": \"54.080556\", \"user_id\": 23, \"name\": \"Eoin Gallagher\", \"longitude\": \"-6.361944\"}");
        objectList.add("{\"latitude\": \"52.986375\", \"user_id\": 12, \"name\": \"Christina McArdle\", \"longitude\": \"-6.043701\"}");
        objectList.add("{\"latitude\": \"51.92893\", \"user_id\": 1, \"name\": \"Alice Cahill\", \"longitude\": \"-10.27699\"}");
    }

    @Test
	public void testIfDataWereConvertedToCustomer() throws IOException {
        Mockito.when(fileStorageService.getDataFromFile(path)).thenReturn(objectList);
        customerList = customersInviteService.getListFromFileAndTransformToCustomersList(path);
        Assert.assertTrue("Test if data were converted to Customer", customerList.get(0) instanceof Customer);
    }

    @Test
	public void testIfListRetrievedIsNoNull() throws IOException {
        Mockito.when(fileStorageService.getDataFromFile(path)).thenReturn(objectList);
        customerList = customersInviteService.getListFromFileAndTransformToCustomersList(path);
        Assert.assertNotNull("Test if Customer list is not null", customerList);
    }

    @Test
	public void testIfListIsNotEmpty() throws IOException {
        Mockito.when(fileStorageService.getDataFromFile(path)).thenReturn(objectList);
        customerList = customersInviteService.getListFromFileAndTransformToCustomersList(path);
        Assert.assertFalse("Test if data were converted to Customer", customerList.isEmpty());
    }

	@Test(expected = IllegalArgumentException.class)
	public void testIfFormatDataIsIncorrect() throws IOException {
        objectList.add("{\"la\": \"54.080556\", \"user_id\": 23, \"names\": \"Eoin Gallagher\", \"longitudefs\": \"-6.361944\"}");
        Mockito.when(fileStorageService.getDataFromFile(path)).thenReturn(objectList);
        customerList = customersInviteService.getListFromFileAndTransformToCustomersList(path);
    }

    @Test
	public void testIfObjectListFromFileIsEmpty() throws IOException {
        objectList = new ArrayList<>();
        Mockito.when(fileStorageService.getDataFromFile(path)).thenReturn(objectList);
        customerList = customersInviteService.getListFromFileAndTransformToCustomersList(path);
        Assert.assertTrue("Test if object list from file is empty", customerList.isEmpty());
    }

    @Test
	public void testIfCustomerListHasTheCorrectData() throws IOException {
        Mockito.when(fileStorageService.getDataFromFile(path)).thenReturn(objectList);
        customerList = customersInviteService.getListFromFileAndTransformToCustomersList(path);
        Assert.assertEquals("Test if customer list has the correct data", 3, customerList.size());
    }

    @Test
    public void testIfCustomerWithinTargetDistanceIsCorrect() {
        Mockito.when(fileStorageService.getDataFromFile(path)).thenReturn(objectList);
        customerList = customersInviteService.getCustomersListToInvite(path);
        Assert.assertEquals("Test if customer quantity to be invited is correct", 2, customerList.size());
    }

    @Test
    public void testIfDistanceDataExist() {
        Mockito.when(fileStorageService.getDataFromFile(path)).thenReturn(objectList);
        customerList = customersInviteService.getCustomersListToInvite(path);
        Assert.assertEquals("Test if customer distance was defined", 42, Math.round(customerList.get(0).getTargetDistance()));
    }

    @Test
    public void testIfUserIdWasSorted() {
        Mockito.when(fileStorageService.getDataFromFile(path)).thenReturn(objectList);
        customerList = customersInviteService.getCustomersListToInvite(path);
        Assert.assertEquals("Test if user id was sorted id 12", 12, customerList.get(0).getUserId().intValue());
        Assert.assertEquals("Test if user id was sorted id 23", 23, customerList.get(1).getUserId().intValue());
    }

}
