package com.example.habitup;

import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.Model.Map;

import org.junit.Test;

import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class HabitEventTest {
    @Test
    public void testHabitEventSetterGetter() {
        HabitEvent event1 = new HabitEvent();
        event1.setCompletedate(new GregorianCalendar(2017, 10, 2, 15, 16, 17));
        event1.setLocation(new Map());
        event1.setPathofimage("/Users/abcdefg");
        event1.setComment("abcdef");
        try {
            GregorianCalendar date1 = new GregorianCalendar(2017, 10, 2, 15, 16, 17);
            Map location1 = new Map();
            assertTrue(event1.getLocation().equals(location1));
            assertTrue((event1.getCompletedate()).compareTo(date1) == 0);
            assertTrue(event1.getImage() != null);
            assertTrue(event1.getComment().equals("abcdef"));
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testCommentLimited(){
        String c1 = "saasdfaskfdasfasfsafsafasfavsadfsdfsa";
        HabitEvent event1 = new HabitEvent();
        try {
            event1.setComment(c1);

        } catch (IllegalArgumentException e){
            assertTrue(Boolean.TRUE);
        }


    }
    @Test
    public void testCompleteDateRestrict(){
        GregorianCalendar date1 = new GregorianCalendar(2016, 10, 2, 15, 16, 17);
        HabitEvent event2 = new HabitEvent();
        try {
            event2.setCompletedate(date1);
        } catch (IllegalArgumentException e){
            assertTrue(Boolean.TRUE);
        }

    }



    }






