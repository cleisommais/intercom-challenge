package com.cleison.itercom.challenge.services;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class NestedArrayToFlatten {
    /**
     * Method to replace several sub levels array to only flatten array
     * Time complexity is O(n*d)
     * where n is the number of elements in the outermost list and d is the maximum number of dimension
     *
     * @param nestedArray
     * @throws Exception
     */
    public List<Integer> replaceNestedArrayToFlattenArray(Object[] nestedArray) {
        List<Integer> listReturn = new ArrayList<>();
        if (nestedArray == null)
            return null;
        log.info("nestedArray: " + Arrays.deepToString(nestedArray));
        for (int i = 0; i < nestedArray.length; i++) {
            //if the current data is an object array, so call the method recursively
            if (nestedArray[i] instanceof Object[]) {
                listReturn.addAll(replaceNestedArrayToFlattenArray((Object[]) nestedArray[i]));
            } else {
                //Only accept integer data
                if (nestedArray[i] instanceof Integer)
                    listReturn.add((Integer) nestedArray[i]);
                else
                    throw new IllegalArgumentException("Data must be instance of Integer");
            }
        }
        return listReturn;
    }


}
