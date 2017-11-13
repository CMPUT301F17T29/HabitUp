package com.example.habitup;

import android.test.ActivityInstrumentationTestCase2;

import com.example.habitup.Model.Attributes;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;


public class AttributesUnitTest {

    @Test
    public void testAttributeInitialization() {
        Attributes attributes = new Attributes(1);
        String[] names = Attributes.getAttributeNames();

        for (String name : names) {
            assertTrue(attributes.contains(name));
            assertTrue(attributes.getValue(name) == 0);
        }
    }
    @Test
    public void testSetAttributeValue() {
        assertTrue(Attributes.getSize() >= 3);
        String attributeName = Attributes.getAttributeNames()[2];

        Attributes attributes = new Attributes(1);

        attributes.setValue(attributeName, 5);
        assertTrue(attributes.getValue(attributeName) == 5);

        attributes.setValue(attributeName, 0);
        assertTrue(attributes.getValue(attributeName) == 0);
    }
    @Test
    public void testIncreaseAttributeValueBy() {
        assertTrue(Attributes.getSize() >= 4);
        String attributeName = Attributes.getAttributeNames()[3];

        Attributes attributes = new Attributes(1);

        attributes.increaseValueBy(attributeName, 2);
        assertTrue(attributes.getValue(attributeName) == 2);

        attributes.increaseValueBy(attributeName, 3);
        assertTrue(attributes.getValue(attributeName) == 5);
    }

}
