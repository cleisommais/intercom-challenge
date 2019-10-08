package com.cleison.itercom.challenge.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class NestedArrayToFlattedTest {

    @Test
    public void replaceNestedArrayToFlattedArrayTest() {
        log.info("Starting NestedArrayToFlattedTest.replaceNestedArrayToFlattedArrayTest");
        NestedArrayToFlatted nestedArrayToFlatted = new NestedArrayToFlatted();
        // [[1,2,[3]],4]
        Object[] nestedArray = {new Object[]{1, 2, new Object[]{3}}, 4};
        List list = new ArrayList();
        System.out.println(Arrays.deepToString(nestedArray));
        nestedArrayToFlatted.replaceNestedArrayToFlattedArray(nestedArray, list);
        System.out.println(Arrays.toString(list.toArray()));
        Assert.assertArrayEquals(new Object[]{1, 2, 3, 4}, list.toArray());
    }

    @Test
    public void replaceNestedArrayToFlattedArrayOnlyIntegerTest() {
        log.info("Starting NestedArrayToFlattedTest.replaceNestedArrayToFlattedArrayOnlyIntegerTest");
        NestedArrayToFlatted nestedArrayToFlatted = new NestedArrayToFlatted();
        // [["A",2,[3]],true]
        Object[] nestedArray = {new Object[]{"A", 2, new Object[]{3}}, true};
        List list = new ArrayList();
        System.out.println(Arrays.deepToString(nestedArray));
        nestedArrayToFlatted.replaceNestedArrayToFlattedArray(nestedArray, list);
        System.out.println(Arrays.toString(list.toArray()));
        Assert.assertArrayEquals(new Object[]{2, 3}, list.toArray());
    }
}