package com.cleison.itercom.challenge.services;

import com.cleison.itercom.challenge.exeptions.FileStorageException;
import com.cleison.itercom.challenge.exeptions.MyFileNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

public class FileStorageServiceTest {

    private FileStorageService fileStorageService;
    private List<Object> list;
    private Path path;

    @Before
    public void setUp() {
        fileStorageService = new FileStorageService();
    }

    @Test
    public void testListRetrievedData() {
        path = FileSystems.getDefault().getPath("src/main/resources/customers.txt").toAbsolutePath();
        list = fileStorageService.getDataFromFile(path);
        Assert.assertNotNull("", list);
        Assert.assertEquals("Test if list retrieved 32 items", 32, list.size());
    }

    @Test
    public void testListIsNotNull() {
        path = FileSystems.getDefault().getPath("src/main/resources/customers.txt").toAbsolutePath();
        list = fileStorageService.getDataFromFile(path);
        Assert.assertNotNull("Test if list retrieved is not null", list);
    }

    @Test(expected = FileStorageException.class)
    public void testPathParameterNull() {
        list = fileStorageService.getDataFromFile(path);
    }

    @Test(expected = MyFileNotFoundException.class)
    public void testWhenFileDoesNotExist() {
        path = FileSystems.getDefault().getPath("src/main/resources/customers.json").toAbsolutePath();
        list = fileStorageService.getDataFromFile(path);
    }

    @Test(expected = MyFileNotFoundException.class)
    public void testWhenDirectoryDoesNotExist() {
        path = FileSystems.getDefault().getPath("src/main/resource/customers.txt").toAbsolutePath();
        list = fileStorageService.getDataFromFile(path);
    }

    @Test
    public void testListIsEmpty() throws IllegalArgumentException {
        path = FileSystems.getDefault().getPath("src/main/resources/customers_empty.txt").toAbsolutePath();
        list = fileStorageService.getDataFromFile(path);
        Assert.assertEquals("Test if list retrieved is empty", true, list.isEmpty());
    }

}
