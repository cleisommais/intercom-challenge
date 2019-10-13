package com.cleison.itercom.challenge.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class NestedArrayToFlattenTest {
    NestedArrayToFlatten nestedArrayToFlatten;
    Integer[] responseExptected;

    @Before
    public void setUp() {
        nestedArrayToFlatten = new NestedArrayToFlatten();
        responseExptected = new Integer[]{1, 2, 3, 4, 5, 6, 7};
    }

    @Test
    public void testNullReturnsNull() throws IllegalArgumentException {
        Assert.assertNull(
                "Testing a null argument",
                nestedArrayToFlatten.replaceNestedArrayToFlattenArray(null)
        );
    }

    @Test
    public void testEmptyArray() throws IllegalArgumentException {
        Assert.assertArrayEquals(
                "Testing an empty array",
                new Integer[]{},
                nestedArrayToFlatten.replaceNestedArrayToFlattenArray(new Object[]{}).toArray()
        );
    }

    @Test
    public void testFlatArray() throws IllegalArgumentException {
        Assert.assertArrayEquals(
                "Testing a flat array",
                responseExptected,
                nestedArrayToFlatten.replaceNestedArrayToFlattenArray(new Object[]{1, 2, 3, 4, 5, 6, 7}).toArray()
        );
    }

    @Test
    public void testNestedArray() throws IllegalArgumentException {
        Assert.assertArrayEquals(
                "Testing nested array",
                responseExptected,
                nestedArrayToFlatten.replaceNestedArrayToFlattenArray(new Object[]{1, 2, 3, 4, new Object[]{5}, 6, 7}).toArray()
        );
    }

    @Test
    public void testMultipleNestedArrays() throws IllegalArgumentException {
        Assert.assertArrayEquals(
                "Testing multiple nested arrays",
                responseExptected,
                nestedArrayToFlatten.replaceNestedArrayToFlattenArray(new Object[]{new Object[]{1, 2, new Object[]{3}}, 4, 5, new Object[]{6}, new Object[]{new Object[]{7}}}).toArray()
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionForObjectInArray() throws IllegalArgumentException {
        nestedArrayToFlatten.replaceNestedArrayToFlattenArray(
                new Object[]{new Object()}
        ).toArray();
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionForObjectInNestedArray() throws IllegalArgumentException {
        nestedArrayToFlatten.replaceNestedArrayToFlattenArray(
                new Object[]{1, true, new Object[]{"A", new Object()}}
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionForNullInArray() throws IllegalArgumentException {
        nestedArrayToFlatten.replaceNestedArrayToFlattenArray(
                new Object[]{null}
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionForNullInNestedArray() throws IllegalArgumentException {
        nestedArrayToFlatten.replaceNestedArrayToFlattenArray(
                new Object[]{1, 2, new Object[]{3, null}}
        );
    }
}
