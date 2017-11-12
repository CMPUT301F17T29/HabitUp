//package com.example.habitup;
//
//import android.support.test.runner.AndroidJUnit4;
//
//import com.example.habitup.Model.HabitEvent;
//import com.example.habitup.Model.HabitEventList;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static junit.framework.Assert.assertFalse;
//import static junit.framework.Assert.assertTrue;
//
///**
// * Created by gojeffcho on 2017-10-20.
// */
//
//@RunWith(AndroidJUnit4.class)
//public class HabitEventListTest {
//
//    @Test
//    public void testAddHabitEvent() {
//        HabitEventList list = new HabitEventList();
//        HabitEvent e = new HabitEvent();
//
//        list.add(e);
//        assertTrue(list.contains(e));
//    }
//
//    @Test
//    public void testHabitEventListSize() {
//        HabitEventList list = new HabitEventList();
//        HabitEvent e = new HabitEvent();
//
//        list.add(e);
//        assertTrue(list.size() == 1);
//
//        HabitEvent e2 = new HabitEvent();
//        list.add(e2);
//        assertTrue(list.size() == 2);
//    }
//
//    @Test
//    public void testHabitEventListContains() {
//        HabitEventList list = new HabitEventList();
//        HabitEvent e = new HabitEvent();
//
//        assertFalse(list.contains(e));
//
//        list.add(e);
//
//        assertTrue(list.contains(e));
//    }
//
//    @Test
//    public void testHabitEventListDelete() {
//        HabitEventList list = new HabitEventList();
//        HabitEvent e = new HabitEvent();
//        HabitEvent e2 = new HabitEvent();
//
//        list.add(e);
//        list.add(e2);
//
//        list.remove(e);
//
//        assertFalse(list.contains(e));
//    }
//
//}
