package com.example.habitup;



import android.test.ActivityInstrumentationTestCase2;

import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitList;

import org.junit.Test;



public class HabitListUnitTest extends ActivityInstrumentationTestCase2 {

    public HabitListUnitTest(){
        super(HabitListUnitTest.class);
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
    public void testAddHabit() {
        HabitList list = new HabitList(1);
        Habit h = new Habit(1);

        list.add(h);
        assertTrue(list.contains(h));
    }

    @Test
    public void testHabitArrayListGet() {
        HabitList list = new HabitList(1);
        Habit h = new Habit(1);
        Habit h2 = new Habit(1);

        list.add(h);
        list.add(h2);

        assertTrue(list.getHabitArrayList().size() == 2);
    }

    @Test
    public void testHabitListSize() {
        HabitList list = new HabitList(1);
        Habit h = new Habit(1,null,null,null,null,null);

        list.add(h);
        assertTrue(list.size() == 1);

        Habit h2 = new Habit(1);
        list.add(h2);
        assertTrue(list.size() == 2);
    }
    @Test
    public void testHabitListContains() {
        HabitList list = new HabitList(1);
        Habit h = new Habit(1);

        assertFalse(list.contains(h));

        list.add(h);

        assertTrue(list.contains(h));
    }
    @Test
    public void testHabitListDelete() {
        HabitList list = new HabitList(1);
        Habit h = new Habit(1);
        Habit h2 = new Habit(1);

        list.add(h);
        list.add(h2);

        list.remove(h);

        assertFalse(list.contains(h));
    }
    @Test
    public void testGetHabit() {
        HabitList list = new HabitList(1);
        Habit h = new Habit(1,"testHabit",null,null,null,null);
        Habit h2 = new Habit(1);

        list.add(h);

        assertTrue(h2 == list.getHabit("testHabit"));
    }



}