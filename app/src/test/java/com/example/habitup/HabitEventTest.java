package com.example.habitup;




import android.test.ActivityInstrumentationTestCase2;


import com.example.habitup.Model.HabitEvent;

import com.example.habitup.Model.Map;


import java.util.GregorianCalendar;

public class HabitEventTest extends ActivityInstrumentationTestCase2 {
    public HabitEventTest() {
        super(HabitEventTest.class);
    }

    public void testHabitEventSetterGetter(){
        HabitEvent event1= new HabitEvent();
        event1.setCompletedate(new GregorianCalendar(2017,10,2,15,16,17));
        event1.setLocation(new Map());
        event1.setImage("/Users/abcdefg");
        event1.setComment("abcdef");
        try{
            GregorianCalendar date1 = new GregorianCalendar(2017,10,2,15,16,17);
            Map location1 = new Map();
            assertTrue(event1.getLocation().equals(location1));
            assertTrue((event1.getCompletedate()).compareTo(date1) ==0);
            assertTrue(event1.getImage()!= null);
            assertTrue(event1.getComment().equals("abcdef"));
        }
        catch(Exception e){
            fail();
        }


    }



}
