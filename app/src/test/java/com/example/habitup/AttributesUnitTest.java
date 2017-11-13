package com.example.habitup;

import android.test.ActivityInstrumentationTestCase2;

import com.example.habitup.Model.Attributes;

import org.junit.Test;



public class AttributesUnitTest extends ActivityInstrumentationTestCase2 {

    public AttributesUnitTest(){
        super(Attributes.class);
    }
    @Override
    public void setUp() throws Exception{

    }

    @Override
    public void tearDown() throws Exception {

    }

    @Override
    public void runTest() throws Exception {

    }
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
