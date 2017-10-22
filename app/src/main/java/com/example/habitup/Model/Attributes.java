package com.example.habitup.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sharibarboza on 2017-10-21.
 *
 * Attribute container to hold a user's attributes. The number of attributes and the attribute
 * names will be the same for every user. If attribute names are changed or if more attributes
 * need to be added, just update the static attributeNames list.
 *
 * Implementation uses a hash map, with the attribute name as the key and an integer
 * number (representing the attribute's points) as the value. A hash map is used to allow quick
 * access to any attribute, such as when only one attribute's value needs to be retrieved/updated.
 */

public class Attributes {

    // Static members
    private final static String[] attributeNames = {
        "Mental",
        "Physical",
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
        attributeMap = new HashMap<String,Integer>();

        for (String name : attributeNames) {
            attributeMap.put(name, 0);
        }
    }

    /**
     * Return the list of attribute names
     * @return the string list of attribute names
     */
    public static String[] getAttributeNames() {
        return attributeNames;
    }

    /**
     * Return the number of attributes
     * @return the list length of attribute names
     */
    public static int getSize() {
        return attributeNames.length;
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
