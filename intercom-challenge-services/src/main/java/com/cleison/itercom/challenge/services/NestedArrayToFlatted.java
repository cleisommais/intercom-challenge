package com.cleison.itercom.challenge.services;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class NestedArrayToFlatted {
    public void replaceNestedArrayToFlattedArray(Object[] nestedArray, List list) {
        log.info("Starting replaceNestedArrayToFlattedArray");
        for (int i = 0; i < nestedArray.length; i++) {
            if (nestedArray[i] instanceof Object[]) {
                replaceNestedArrayToFlattedArray((Object[]) nestedArray[i], list);
            } else {
                if (nestedArray[i] instanceof Integer)
                    list.add(nestedArray[i]);
            }
        }
    }
}
