//package com.example.habitup;
//
//import android.support.test.runner.AndroidJUnit4;
//
//import com.example.habitup.Model.Habit;
//import com.example.habitup.Model.HabitEvent;
//import com.example.habitup.Model.HabitEventList;
//import com.example.habitup.Model.HabitList;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static junit.framework.Assert.assertFalse;
//import static junit.framework.Assert.assertTrue;
//
///**
// * Created by gojeffcho on 2017-10-21.
//*/
//
//@RunWith(AndroidJUnit4.class)
//public class HabitListTest {
//
//    @Test
//    public void testAddHabit() {
//        HabitList list = new HabitList();
//        Habit h = new Habit();
//
//        list.add(h);
//        assertTrue(list.contains(h));
//    }
//
//    @Test
//    public void testHabitListSize() {
//        HabitList list = new HabitList();
//        Habit h = new Habit();
//
//        list.add(h);
//        assertTrue(list.size() == 1);
//
//        Habit h2 = new Habit();
//        list.add(h2);
//        assertTrue(list.size() == 2);
//    }
//
//    @Test
//    public void testHabitListGet() {
//        HabitList list = new HabitList();
//        Habit h = new Habit();
//        Habit h2 = new Habit();
//
//        list.add(h);
//        list.add(h2);
//
//        assertTrue(list.getHabits().size() == 2);
//    }
//
//    @Test
//    public void testHabitListContains() {
//        HabitList list = new HabitList();
//        Habit h = new Habit();
//
//        assertFalse(list.contains(h));
//
//        list.add(h);
//
//        assertTrue(list.contains(h));
//    }
//
//    @Test
//    public void testHabitListDelete() {
//        HabitList list = new HabitList();
//        Habit h = new Habit();
//        Habit h2 = new Habit();
//
//        list.add(h);
//        list.add(h2);
//
//        list.remove(h);
//
//        assertFalse(list.contains(h));
//    }
//
//}
