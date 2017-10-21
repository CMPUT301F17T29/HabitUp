package com.example.habitup;

import android.support.test.runner.AndroidJUnit4;

import com.example.habitup.Model.Attributes;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;

/**
 * Created by sharibarboza on 2017-10-21.
 */

@RunWith(AndroidJUnit4.class)
public class AttributesTest {

    @Test
    public void testAttributeInitialization() {
        Attributes attributes = new Attributes();
        String[] names = {"Intelligence", "Strength", "Social", "Discipline"};

        for (String name : names) {
            assertTrue(attributes.contains(name));
            assertTrue(attributes.getValue(name) == 0);
        }

    }

    @Test
    public void testSetAttributeValue() {
        Attributes attributes = new Attributes();

        attributes.setValue("Social", 5);
        assertTrue(attributes.getValue("Social") == 5);

        attributes.setValue("Social", 0);
        assertTrue(attributes.getValue("Social") == 0);
    }

    @Test
    public void testIncreaseAttributeValueBy() {
        Attributes attributes = new Attributes();

        attributes.increaseValueBy("Strength", 2);
        assertTrue(attributes.getValue("Strength") == 2);

        attributes.increaseValueBy("Strength", 3);
        assertTrue(attributes.getValue("Strength") == 5);
    }
}
