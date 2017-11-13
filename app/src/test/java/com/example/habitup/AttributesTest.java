//package com.example.habitup;
//
//import android.support.test.runner.AndroidJUnit4;
//
//import com.example.habitup.Model.Attributes;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static junit.framework.Assert.assertTrue;
//
///**
// * Created by sharibarboza on 2017-10-21.
// */
//
//@RunWith(AndroidJUnit4.class)
//public class AttributesTest {
//
//    @Test
//    public void testAttributeInitialization() {
//        Attributes attributes = new Attributes();
//        String[] names = Attributes.getAttributeNames();
//
//        for (String name : names) {
//            assertTrue(attributes.contains(name));
//            assertTrue(attributes.getValue(name) == 0);
//        }
//    }
//
//    @Test
//    public void testSetAttributeValue() {
//        assertTrue(Attributes.getSize() >= 3);
//        String attributeName = Attributes.getAttributeNames()[2];
//
//        Attributes attributes = new Attributes();
//
//        attributes.setValue(attributeName, 5);
//        assertTrue(attributes.getValue(attributeName) == 5);
//
//        attributes.setValue(attributeName, 0);
//        assertTrue(attributes.getValue(attributeName) == 0);
//    }
//
//    @Test
//    public void testIncreaseAttributeValueBy() {
//        assertTrue(Attributes.getSize() >= 4);
//        String attributeName = Attributes.getAttributeNames()[3];
//
//        Attributes attributes = new Attributes();
//
//        attributes.increaseValueBy(attributeName, 2);
//        assertTrue(attributes.getValue(attributeName) == 2);
//
//        attributes.increaseValueBy(attributeName, 3);
//        assertTrue(attributes.getValue(attributeName) == 5);
//    }
//}
