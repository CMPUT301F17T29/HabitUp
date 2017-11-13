package com.example.habitup;

import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;


import com.example.habitup.Model.HabitEvent;
import com.example.habitup.Model.HabitEventList;

import org.junit.Test;



public class HabitEventListUnitTest extends ActivityInstrumentationTestCase2 {

    public HabitEventListUnitTest(){
        super(HabitEventList.class);
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
    public void testAddHabitEvent() {
        HabitEventList list = new HabitEventList(1,1);
        HabitEvent e = new HabitEvent(1,1);

        list.add(e);
        assertTrue(list.contains(e));
    }
    @Test
    public void testHabitEventListSize() {
        HabitEventList list = new HabitEventList(1,1);
        HabitEvent e = new HabitEvent(1,1);

        list.add(e);
        assertTrue(list.size() == 1);

        HabitEvent e2 = new HabitEvent(1,1);
        list.add(e2);
        assertTrue(list.size() == 2);
    }

   @Test
    public void testHabitEventListContains() {
        HabitEventList list = new HabitEventList(1,1);
        HabitEvent e = new HabitEvent(1,1);

        assertFalse(list.contains(e));

        list.add(e);

        assertTrue(list.contains(e));
    }

    @Test
    public void testHabitEventListDelete() {
        HabitEventList list = new HabitEventList(1,1);
        HabitEvent e = new HabitEvent(1,1);
        HabitEvent e2 = new HabitEvent(1,1);

        list.add(e);
        list.add(e2);

        list.remove(e);

        assertFalse(list.contains(e));
    }

    @Test
    public void testGetTodaysHabitArrayList(){


    }



}