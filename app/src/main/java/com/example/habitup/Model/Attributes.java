package com.example.habitup.Model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by sharibarboza on 2017-10-21.
 *
 * Attribute container for
 */

public class Attributes {

    // Static members
    private final static String[] attributeNames = {
        "Intelligence",
        "Strength",
        "Social",
        "Discipline"
    };

    // Members
    private Map<String,Integer> attributeMap;

    /**
     * Attributes constructor, which takes the class variable attributeNames
     * and populates an initialized attributeMap with default values of 0.
     */
    public Attributes() {} {
        attributeMap = new LinkedHashMap<String,Integer>();

        for (String name : attributeNames) {
            attributeMap.put(name, 0);
        }
    }

    /**
     * Returns the attributes linked hash map, which can be iterated
     * in the same order as the attributeNames list.
     * @return
     */
    public Map<String,Integer> getAttributes() {
        return attributeMap;
    }

    /**
     * Checks if the attribute hash map contains a key with a specific attribute name.
     * @param name the name of the attribute
     * @return True if the attribute key name exists
     */
    public boolean contains(String name) {
        return attributeMap.containsKey(name);
    }

    /**
     * Returns the value of a particular attribute.
     * @param name the attribute name
     * @return the attribute value
     */
    public Integer getValue(String name) {
        return attributeMap.get(name);
    }

    /**
     * Updates an attribute by setting it to a different value.
     * @param name the name of the attribute
     * @param value the new attribute's value
     */
    public void setValue(String name, Integer value) {
        attributeMap.put(name, value);
    }

    /**
     * Increase an attributes value by a specified amount.
     * @param name the name of the attribute
     * @param value what to add to the attribute's current value
     */
    public void increaseValueBy(String name, Integer value) {
        Integer oldValue = this.getValue(name);
        attributeMap.put(name, oldValue + value);
    }

}
